# coding: utf-8
import math
import os
import threading
import time
import random
from random import choice

import cv2
import numpy as np
from matplotlib import pyplot as plt

from networktables import NetworkTables


def gray(img):
    return cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)


def cropValue(img, y_index):
    WIDTH_INDEX = 1
    HEIGHT_CONST = 1

    shape = img.shape
    img_height = shape[0]
    img_width = shape[1]

    x = img_width*(1-WIDTH_INDEX)*0.5
    w = img_width*WIDTH_INDEX
    y = img_height*y_index
    h = HEIGHT_CONST

    a = [x, w, y, h]
    return map(math.floor, a)


def cropImg(img, x, w, y, h, dispImg=None):
    return (img[y:y+h, x:x+w])


def findIntersections(crop_img_line):

    _, binary = cv2.threshold(crop_img_line, 200, 1, cv2.THRESH_BINARY)
    x1 = np.nonzero(binary)[1]
    x0 = np.where(binary == 0)[1]

    bestStartIdx = 0
    bestEndIdx = -1

    startIdx = 0
    endIdx = -1

    while True:
        if x1[x1 > endIdx].size:
            startIdx = x1[x1 > endIdx][0]
        else:
            break
        if x0[x0 > startIdx].size:
            endIdx = x0[x0 > startIdx][0]
        else:
            break
        if endIdx-startIdx > bestEndIdx-bestStartIdx:
            bestStartIdx = startIdx
            bestEndIdx = endIdx

    if bestStartIdx > 0:
        brightPt = math.floor((bestStartIdx+bestEndIdx)/2)
        return [brightPt]
    else:
        return [0]


def find_line_parameters(X, Y):
    #
    # Takes clean points
    # Returns line best fit (After outlier_rem algorithm)
    #

    SLOPE_SEN_START_CONST = 0.05
    MAX_SLOPE_SEN_CONST = 0.4
    SLOPE_SEN_STEP_CONST = 0.05

    slope_sensitivity = SLOPE_SEN_START_CONST

    # Xraw = [pt[0] for pt in raw_pts]
    # Yraw = [pt[1] for pt in raw_pts]

    # X, Y = find_best_pt_group(Xraw, Yraw)

    if len(X) == 0:
        print("no valid group found, failed")
        return 0
    # grouped_pts = raw_pts

    # Only the largest group will be used for the linear fit
    while True:
        line_param = find_best_fit_line(X.copy(), Y.copy(), slope_sensitivity)
        if slope_sensitivity > MAX_SLOPE_SEN_CONST:
            print("max sensitivity reached, can't locate the band")
            return 0
        elif line_param == 0:
            slope_sensitivity = slope_sensitivity + SLOPE_SEN_STEP_CONST
            print('try increase sensitivity to ', slope_sensitivity)
        else:
            return line_param  # SUCCESS


def find_best_pt_group(Xraw, Yraw):
    #
    # TAKES RAW POINTS
    # RETURNS POINTS OF BEST GROUP
    #
    DIST_RANGE_CONST = 40
    LEAST_POINTS_CONST_INT = 4

    MAX_PROPOGATE_CONST_INT = 15
    MAX_SAMPLE_CONST_INT = 10
    GRID_DENSITY_CONST_INT = 16

    def dist(x, y):
        return ((x[1]-x[0])**2+(y[1]-y[0])**2)**0.5

    if len(Xraw) != len(Yraw):
        print('ivalid data input')
        return 0

    sample_count = 0
    while sample_count < MAX_SAMPLE_CONST_INT:
        indexList = [i for i in range(0, len(Xraw))]

        selected_group = []

        # First random choice
        randIndex = choice(indexList)
        indexList.remove(randIndex)
        selected_group.append(randIndex)

        propogate_count = 0
        while propogate_count < MAX_PROPOGATE_CONST_INT:
            # Do one propogate
            thisIdx = choice(selected_group)
            for i in indexList:
                if dist((Xraw[i], Xraw[thisIdx]), (Yraw[i], Yraw[thisIdx])) < DIST_RANGE_CONST:
                    indexList.remove(i)
                    selected_group.append(i)
            propogate_count = propogate_count+1
        if len(selected_group) > LEAST_POINTS_CONST_INT:
            return ([Xraw[i] for i in selected_group], [Yraw[i] for i in selected_group])
        sample_count = sample_count+1
    print('valid group not found')
    return [], []


def findForSingleIndex(img, y_index):
    #
    # OUTPUTS RAW POINTS
    #

    points = []
    [x, w, y, h] = cropValue(img, y_index)

    grayLine = cropImg(img, x, w, y, h)

    [points.append((intersect_x, y))
     for intersect_x in findIntersections(grayLine) if intersect_x != 0]

    return points

# # Determine width of grid

#     Xrange = max(Xraw)-min(Xraw)
#     Yrange = max(Yraw)-min(Yraw)

#     grid_step_X = math.ceil(Xrange/GRID_DENSITY_CONST_INT)
#     grid_step_Y = math.ceil(Yrange/GRID_DENSITY_CONST_INT)

#     grid_count = np.zeros(
#         (GRID_DENSITY_CONST_INT, GRID_DENSITY_CONST_INT, 0))

#     for i in range(0, len(Xraw)):
#         x, y=Xraw[i], Yraw[i]
#         xGrid, yGrid=map(math.floor, [x//grid_step_X, y//grid_step_Y])
#         np.append(grid_count[xGrid][yGrid], i)

#     indices=grid_count.flatten()[np.argmax(
#         np.count_nonzero(grid_count, axis=(0, 1)))]

    # def findGroup(idx):
    #     group_indices = []
    #     group_check = []

    #     def propogateOnce(idx, group_indices, pts):
    #         to_Add = []
    #         for i, pt in enumerate(pts):
    #             if dist(pts[idx], pt) < DIST_RANGE_CONST and i not in group_indices:
    #                 to_Add.append(i)
    #         return to_Add

    #     extra = propogateOnce(idx, group_indices, pts)
    #     group_indices = group_indices + extra
    #     group_check = group_check + extra

    #     while len(group_check):
    #         extra = propogateOnce(group_check.pop(), group_indices, pts)
    #         group_indices = group_indices + extra
    #         group_check = group_check + extra

    #     group = [pts[index] for index in group_indices]
    #     return group  # Return list of all points

    # max_group_length = 0
    # max_group = []
    # while len(pts) > 0:
    #     # create a new group with a random idx
    #     idx = 0
    #     this_group = findGroup(idx)
    #     pts = [pt for pt in pts if pt not in this_group]
    #     if len(this_group) > max_group_length:
    #         max_group = this_group
    #         max_group_length = len(this_group)
    #     if max_group_length > len(pts):
    #         break
    # if len(max_group) < LEAST_POINTS_CONST:
    #     print("no point group found")
    #     return 0

    # return max_group


def find_best_fit_line(X, Y, slope_sensitivity):
    #
    # Takes points and sensitivity index
    # Recursively run a outlier_remove algorithm
    # Returns 0 if not successful
    # Returns [k,b] if successful
    #
    MAX_REMOVE_CONST = 20
    doRemoveOutliers = True  # for debug
    doDebugOutput = False  # for debug

    # Linear Fit

    if doDebugOutput:
        plt.scatter(X, Y, s=75, alpha=.5)

    # Remove Outliers

    def fit_and_plot():
        lineFit = np.polyfit(X, Y, 1)
        line = list(lineFit)
        if doDebugOutput:
            p = np.poly1d(lineFit)
            xp = np.linspace(100, 300)
            plt.plot(xp, p(xp))
        return line

    # Linear fit and get coefficient
    line = fit_and_plot()
    k1 = line[0]

    count = 0

    if doDebugOutput:
        print([count, k1])

    while doRemoveOutliers:

        count = count + 1

        line = fit_and_plot()
        k2 = line[0]

        if doDebugOutput:
            print([count, k2])
            print('[dif,sen,len]')
            print([abs(math.atan(k2)-math.atan(k1)), slope_sensitivity, len(X)])

        if (abs(math.atan(k2)-math.atan(k1)) < slope_sensitivity)or (abs(math.atan(k2)+math.atan(k1)) < slope_sensitivity):
            if doDebugOutput:
                print('success! return result')
            break
        elif(count > MAX_REMOVE_CONST):
            if doDebugOutput:
                print('max remove reached, not found')
            return 0
        elif(len(X) == 1):
            if doDebugOutput:
                print('all point deleted, not found')
            return 0
        else:
            k1 = k2

        # Determine Outlier Element
        # @profile
        def sensitivity(indices):
            indices.reverse()
            XX = X.copy()
            YY = Y.copy()
            for i in indices:
                del XX[i]
                del YY[i]
            new_k = np.polyfit(XX, YY, 1)[0]
            return abs(new_k - k1)

        D = [sensitivity([i]) for i in range(0, len(X))]
        maxIndex = D.index(max(D))

        # Delete Element and judge
        del X[maxIndex]
        del Y[maxIndex]

    [k, b] = [line[0], line[1]]  # JUST FOR TEST, OUTIER NOT IMPLEMENTED
    if doDebugOutput:
        plt.draw()

    lineParam = [k, b]
    return lineParam


# Networktables Setup
cond = threading.Condition()
notified = [False]


def connectionListener(connected, info):
    print(info, '; Connected=%s' % connected)
    with cond:
        notified[0] = True
        cond.notify()


NetworkTables.initialize(server='10.55.15.2')
NetworkTables.addConnectionListener(connectionListener, immediateNotify=True)

with cond:
    print("Waiting")
    if not notified[0]:
        cond.wait()

print("Connected!")
table = NetworkTables.getTable('datatable')


# Stablization config
delK_THRESHOLD = 0.7
MAX_KEEP_CONST = 5
CACHE_CONST = 5

# IMG config
IMG_SPLIT_PRECISION = 40
FRAME_SIZE_INDEX = 1

# cam setup
cap = cv2.VideoCapture(0)

# Runtime Setup
k_cache = []
last_k = 0
last_b = 0
keep_count = 0
idx = 0


def paramProcess(k, b, x, y, idx):
    # Ttheta
    atank = math.atan(k)
    if atank < 0:
        theta = math.pi/2+atank
    else:
        theta = -math.pi/2+atank

    # L-R
    DEEP_DARK_INDEX = 1

    x0 = x/2
    y0 = math.floor(DEEP_DARK_INDEX*y)+y

    if k*x0/2 + b < y0:
        LR = 1  # pt <-->  line
    else:
        LR = -1  # line <--> pt

    A,B,C=k,-1,b
    
    dist = abs(A*x0+B*y0+C)/(A**2+B**2)**0.5

    return [

        ['imgproc_index', idx],
        ['imgproc_theta', theta],
        ['imgproc_LR', LR],
        ['imgproc_dist', dist],
        ['imgproc_sizex', x],
        ['imgproc_sizey', y]

    ]

KKK =[]

BBB = []
print("huhuhu")

# if __name__=="__main__":
stopflag= 0
beginflag = 0
class myThread(threading.Thread):
    def __init__(self,threadId):
        threading.Thread.__init__(self)
        self.img = None
        self.threadId = threadId
        self.stopflag= 0
        self.beginflag = 0
        print(self.threadId)
    def run(self):
        if self.threadId == 0:
            while stopflag == 0:
                _,thisImg = cap.read()
                if ~(thisImg==None):
##                    print('get image')
                    # threadLock.acquire()
                    self.img = thisImg
                    # threadLock.release()
                    beginflag = 1
        elif self.threadId == 1:
            
            if beginflag==1:
                # threadLock.acquire()
                processEachImage(self.img)
                # threadLock.release()
##                print('process image')


        
def processEachImage(frame):
    idx = 0
    while(True):
        idx = idx + 1
        # get a frame



        # _, frame = cap.read()
        height, width = frame.shape[:2]
        frame = cv2.resize(frame, (math.floor(
            FRAME_SIZE_INDEX*width), math.floor(FRAME_SIZE_INDEX*height)))
        height, width = frame.shape[:2]
        # cv2.imshow("capture", frame)
    # FIND-INTERSECTIONS & RAW POINTS
        # Read all intersect found
        # Generate all Y Index
        y_indices = [x/IMG_SPLIT_PRECISION for x in range(1, IMG_SPLIT_PRECISION)]

        BRIGHTNESS_CONST = 245

        blur = cv2.GaussianBlur(gray(frame), (11, 11), 0)
        _, thresh = cv2.threshold(blur, BRIGHTNESS_CONST, 255, cv2.THRESH_BINARY)
        eroded = cv2.erode(thresh, None, iterations=5)
        final = eroded

    # pool = Pool(1)
    ##
    # results = []
    # [results.append(pool.apply_async(findForSingleIndex, args=(final,y_index))) for y_index in y_indices]
    ##
    # pool.close()
    # pool.join()
    ##
    # raw_pts=[]
    #        [raw_pts.extend(li) for li in [result.get() for result in results]]

        raw_pts = []
        [raw_pts.extend(li) for li in [findForSingleIndex(final, y_index)
                                    for y_index in y_indices]]

        if len(raw_pts) == 0:
            print("no any point found, failed")
        else:

            Xraw, Yraw = [pt[0] for pt in raw_pts], [pt[1] for pt in raw_pts]
            X, Y = find_best_pt_group(Xraw, Yraw)
            line_param = find_line_parameters(X, Y)

            if line_param != 0:
                k, b = line_param
            #     KKK.append(k)
            #     BBB.append(b)
            #     if len(k_cache) == 0 or keep_count > MAX_KEEP_CONST:
            #         k_cache = [abs(math.atan(k)) for _ in range(0, CACHE_CONST)]
            #         keep_count = 0
            #     elif abs(np.mean(k_cache)-math.atan(k)) < delK_THRESHOLD or abs(np.mean(k_cache)+math.atan(k)) < delK_THRESHOLD:
            #         k_cache.pop(0)
            #         k_cache.append(abs(math.atan(k)))
            #         line_p1 = (0, math.floor(b))
            #         line_p2 = (1000, math.floor(1000*k + b))
            #         cv2.line(frame, line_p1, line_p2, (255, 0, 0), 2)

            #         last_k = k
            #         last_b = b
            #     else:
            #         print('break threshold')
            #         keep_count = keep_count+1
            #         k, b = last_k, last_b
                line_p1 = (0, math.floor(b))
                line_p2 = (1000, math.floor(1000*k + b))
                cv2.line(frame, line_p1, line_p2, (255, 0, 0), 2)

                ctrlParamList = paramProcess(k, b, height, width, idx)

                for param in ctrlParamList:
                    print("{0} is equal to {1}".format(param[0], param[1]))
                    table.putNumber(param[0], param[1])
        cv2.imshow("capture", frame)
        if cv2.waitKey(1) & 0xFF == ord('q'):
            stopflag=1
            plt.plot(KKK,BBB)
            print(KKK)
            print(BBB)
            break
        while True:
            if cv2.waitKey(1) & 0xFF == ord('d'):
                #data add
                break


    cap.release()
    cv2.destroyAllWindows()
threadLock = threading.Lock()
threads = []
# 创建新线程
thread1 = myThread(0)
thread2 = myThread(1)
# 开启新线程
thread1.start()
thread2.start()
# 添加线程到线程列表
threads.append(thread1)
threads.append(thread2)
# 等待所有线程完成
for t in threads:
    t.join()
print ("主进程结束！")
