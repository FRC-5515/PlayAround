
# coding: utf-8

# In[1]:

import cv2
from matplotlib import pyplot as plt
import math
import numpy as np
import time
#from IPython import display
import os
#from numba import autojit
#import jenkspy
#from sklearn.cluster import KMeans
import threading
from networktables import NetworkTables


# In[2]:

def gray(img):
    return cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
#@profile 
def cropValue(img,y_index):
    WIDTH_INDEX = 1
    HEIGHT_CONST = 1
    
    shape = img.shape
    img_height = shape[0]
    img_width = shape[1]
    
    x=img_width*(1-WIDTH_INDEX)*0.5
    w=img_width*WIDTH_INDEX
    y=img_height*y_index
    h=HEIGHT_CONST
    
    a = [x,w,y,h,img_width*WIDTH_INDEX]
    
    a1=map(math.floor,a)
    
    return a1
#@profile 
def cropImg(img,x,w,y,h,dispImg = None):
##    if isinstance(dispImg, np.ndarray):
##        cv2.rectangle(dispImg, (x, y), (x+w, y+h), (255,255,255))
    return (img[y:y+h, x:x+w])


# In[3]:

#@profile 
def getBrightPoint(crop_img_line):

    MIN_LENGTH_CONST=5
    #这段算法要重新实现

#   tooBrightBoolList = [x>BRIGHTNESS_CONST for x in grayLine]

  

 #   print(binary)
    
##    lineBinary2 = 0  
##     print(tooBrightBoolList)
##    for i,element in enumerate(binary):
##        if element:
##            lineBinary2 = lineBinary2 | 1<<i

    def list_to_int(test):
        teststr = ''.join(map(str,test))
        testbin = int(teststr.encode("utf-8"), 2)
        return testbin

    ret, binary = cv2.threshold(crop_img_line,200,1,cv2.THRESH_BINARY)  
    
    binary = list(binary[0])
    
##    
    binary.reverse()
    lineBinary = list_to_int(binary)
    

##    print (lineBinary)
    length = 0
    startDigit = -1
    while 0!=((lineBinary-1)&lineBinary): ##一直执行直到二进制串里只有一个1
        lineBinary = lineBinary & lineBinary>>1
        length = length +1
    if lineBinary==0 or length < MIN_LENGTH_CONST:
        return 0

    while 0!=lineBinary: ##当它还不是0
        if lineBinary>>64!=0:
            lineBinary = lineBinary>>64
            startDigit = startDigit + 64
            continue
            
        if lineBinary>>32!=0:
            lineBinary = lineBinary>>32
            startDigit = startDigit + 32
            
        if lineBinary>>16!=0:
            lineBinary = lineBinary>>16
            startDigit = startDigit + 16
            
        if lineBinary>>8!=0:
            lineBinary = lineBinary>>8
            startDigit = startDigit + 8
            
        if lineBinary>>4!=0:
            lineBinary = lineBinary>>4
            startDigit = startDigit + 4
            
        if lineBinary>>2!=0:
            lineBinary = lineBinary>>2
            startDigit = startDigit + 2

        lineBinary = lineBinary>>1
        startDigit = startDigit + 1

    brightPt = startDigit +(length//2)
##    print(('brightPtValue',brightPt))
    return brightPt
        


# In[4]:
#@profile
def findIntersections(img_line):
    NEIGHBOUR_CONST = 5
    MIN_LENGTH_CONST = 5

    return [getBrightPoint(img_line)]

##    y,x=np.nonzero(img_line)
####    print(x)
##    if x.size<5:return [0]

#现在要过滤小的噪点
##    mean = x.mean()
##    std = np.std(x, ddof=1)

##    x = x[abs(x-mean)<3*std]

    

##    breaks = jenkspy.jenks_breaks(x, nb_class=3)
##    print(breaks)
##    interestPoints=[]
##    for i,brk in enumerate(breaks):
##        if i==0: continue
##        last = breaks[i-1]
##        values = x[x<brk]
##        values = values[values>last]
##        if values.size<MIN_LENGTH_CONST:continue
##        if values.size>0: interestPoints.append(values.mean())
##    print(interestPoints)
##    return interestPoints



##    focuses = [mean]
##    clusters_val = 1
##    while [[abs(fk-xval) < NEIGHBOUR_CONST for xval in x]==[] for fk in focuses]:
##        clusters_val = clusters_val + 1
##        estimator = KMeans(n_clusters = clusters_val)
##        
##        estimator.fit(x.reshape(-1,1))
##        print((clusters_val, estimator.cluster_centers_))
##        print((estimator.labels_))


##    estimator = KMeans(n_clusters = 3)
##        
##    estimator.fit(x.reshape(-1,1))
##    print(( estimator.cluster_centers_))
##    print((estimator.labels_))
##    toReturn= [list(arr)[0] for arr in list(estimator.cluster_centers_)]
##    print(toReturn)
##    return(toReturn)
    

# In[5]:
#@profile
def findIntersectionList(img,y_indices):
    BRIGHTNESS_CONST = 245

    
    blur = cv2.GaussianBlur(gray(img), (11, 11), 0)
    ret, thresh = cv2.threshold(blur,BRIGHTNESS_CONST,255,cv2.THRESH_BINARY)
    eroded = cv2.erode(thresh, None, iterations=5)
    final = eroded
##    plt.imshow(eroded)
##    plt.show()
    points = []
    for y_index in y_indices:

        [x,w,y,h,band_width] = cropValue(img,y_index)
        grayLine=cropImg(final,x,w,y,h)

        
        for intersect_x in findIntersections(grayLine):

            if intersect_x!=0: points.append((intersect_x,y))

    if len(points)==0:
        return 0
    else:
        return points
    
# cv2.line(img_disp,line_p1,line_p2,(0,0,0),4);

# plt.imshow(img_disp)
# plt.show()


# In[6]:

def findParameters_rem_outlier(img,precision = 40):
    SLOPE_SEN_START_CONST = 0.05
    MAX_SLOPE_SEN_CONST = 0.4
    SLOPE_SEN_STEP_CONST = 0.05
    
    NEIGHBOUR_PIXEL_CONST = 20
    
    slope_sensitivity = SLOPE_SEN_START_CONST
    

    ##Read all intersect found
    ##Generate all Y Index
    y_indices = [x/precision for x in range(1, precision)]
    raw_pts = findIntersectionList(img,y_indices)
##    print ('raw_pts',raw_pts)
    
    if raw_pts == 0:
        print("no any point found, failed")
        return 0   
    
    grouped_pts = find_point_group(raw_pts)
    
    if grouped_pts == 0:
        print("no valid group found, failed")
        return 0
    
    
    ##Only the largest group will be used for the linear fit
    X = [grouped_pts[i][0] for i in range(0,len(grouped_pts))]
    Y = [grouped_pts[i][1] for i in range(0,len(grouped_pts))]
    
#     print(('X,Y',X,Y))
    
    
#  try:
    a = findBestFit_rem_outlier(X.copy(),Y.copy(), precision,slope_sensitivity)

    while a==0:
        if slope_sensitivity > MAX_SLOPE_SEN_CONST: 
            print("max sensitivity reached, can't locate the band")
            return 0
        slope_sensitivity = slope_sensitivity + SLOPE_SEN_STEP_CONST
        print('try increase sensitivity to ',slope_sensitivity)

        a = findBestFit_rem_outlier(X.copy(),Y.copy(), precision,slope_sensitivity)
    return a
# except:
    print("error, can't locate the band")
    return 0
    
 
def find_point_group(pts):
    DIST_RANGE_CONST = 50
    LEAST_POINTS_CONST = 1
    
    def dist2(pt1,pt2):
        [x1,y1] = pt1
        [x2,y2] = pt2
        return ((x2-x1)**2+(y2-y1)**2)**0.5
    
    def findGroup(idx):
        group_indices = []
        group_check = []
        
        def propogateOnce(idx,group_indices,pts):
            to_Add = []
#             print ('idx is',idx)
            for i,pt in enumerate(pts):
                if dist2(pts[idx],pt)<DIST_RANGE_CONST and i not in group_indices:
                    to_Add.append(i)
    #                     del pts[i]
            return to_Add
        
        extra = propogateOnce(idx,group_indices,pts)
        group_indices = group_indices + extra
        group_check = group_check + extra
        
        while len(group_check):
            extra = propogateOnce(group_check.pop(),group_indices,pts)
            group_indices = group_indices + extra
            group_check = group_check + extra
        
        group=[pts[index] for index in group_indices]
#         print ('group_found',group)
        return group ##Return list of all points
        
        
    groups = []
    max_group_length = 0
    max_group = []
    while len(pts)>0:
        #create a new group with a random idx
        idx = 0
        this_group = findGroup(idx)
        pts = [pt for pt in pts if pt not in this_group]
        if len(this_group)>max_group_length:
            max_group=this_group
            max_group_length=len(this_group)
        if max_group_length>len(pts): break
#     group_lengths = [len(g) for g in groups]
#     max_group = groups[group_lengths.index(max(group_lengths))]
    if len(max_group)<LEAST_POINTS_CONST:
        print("no point group found")
        return 0
    
#     print('max group is',max_group)
    return max_group
        
        



 
def findBestFit_rem_outlier(X,Y,precision,slope_sensitivity):
    MAX_REMOVE_CONST = 20
    doRemoveOutliers=True #for debug
    doDebugOutput = False #for debug       
    

#Linear Fit

    if doDebugOutput: 
        plt.scatter(X, Y, s=75, alpha=.5)
        
#         print('X,Y',X,Y)

#Remove Outliers
    def dist_to_line(pt,line):
        # [x-val,y-val],[x^1,x^0]
        x0=pt[0]
        y0=pt[1]
        A=line[0].item()
        B=float(-1)
        C=line[1].item()
        dist2 = (abs(A*x0+B*y0+C)**2)/(A**2+B**2)
        return dist2

    def fit_and_plot():
        lineFit =np.polyfit(X, Y, 1)
        line = list(lineFit)
        if doDebugOutput:
            p=np.poly1d(lineFit)
            xp=np.linspace(100, 300)
            plt.plot(xp,p(xp))
        return line

    ##Linear fit and get coefficient
    line =fit_and_plot()
    k1 = line[0]

    count = 0

    if doDebugOutput: print([count,k1])

    while doRemoveOutliers:

        count = count + 1
        
        line = fit_and_plot()
        k2 = line[0]

        if doDebugOutput:
            print([count,k2])
            print('[dif,sen,len]')
            print([abs(math.atan(k2)-math.atan(k1)),slope_sensitivity,len(X)])

        if (abs(math.atan(k2)-math.atan(k1)) < slope_sensitivity)or (abs(math.atan(k2)+math.atan(k1)) < slope_sensitivity):
            if doDebugOutput: print('success! return result')
            break
        elif(count>MAX_REMOVE_CONST):
            if doDebugOutput: print('max remove reached, not found')
            return 0
        elif(len(X)==1):
            if doDebugOutput: print('all point deleted, not found')
            return 0
        else:
            k1 = k2

        ##Determine Outlier Element
        #@profile 
        def sensitivity(indices):
            indices.reverse()
#             print(indices)
            XX = X.copy()
            YY = Y.copy()
            for i in indices:
                del XX[i]
                del YY[i]
            new_k = np.polyfit(XX,YY,1)[0]
            return abs(new_k - k1)

        D = [sensitivity([i]) for i in range (0,len(X))]
        maxIndex = D.index(max(D))

        ##Delete Element and judge
        del X[maxIndex]
        del Y[maxIndex]

    [k,b]=[line[0],line[1]] #JUST FOR TEST, OUTIER NOT IMPLEMENTED
    if doDebugOutput: plt.draw()
    return_val = [k,b]
    return return_val


# In[7]:


def test_for_time(img_name,precision):
    ##TESTING
    img = cv2.imread(img_name)
    plt.imshow(img)

##    plt.imshow(img)
    start = time.clock()
    print(
    findParameters_rem_outlier(img,precision = precision)
    )
    plt.show()
    end = time.clock()
    print('precision, time used',precision, end-start)
    return end-start


# In[8]:

##PRECISIONS = [5*x for x in range(1,20)]
##TIMES = [test_for_time(r"C:\Users\felix\Desktop\hahahaha_fucked.png",prec) for prec in PRECISIONS]
##
##
##plt.scatter(PRECISIONS,TIMES)
##plt.show()
##@profile
##def test_time(precision):
##    for i in range(1,1000):
##        test_for_time(r"C:\Users\felix\Desktop\hahahaha_fucked.png",precision)
##
##test_time(40)
##test_for_time(r"C:\Users\felix\Desktop\hahahaha_fucked.png",40)

#Networktables Setup


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

# Insert your processing code here
print("Connected!")
#Networktable Data Put
table = NetworkTables.getTable('datatable')


delK_THRESHOLD = 0.7
MAX_KEEP_CONST = 5
CACHE_CONST = 5

cap = cv2.VideoCapture(1)
k_cache = []
last_k = 0
last_b = 0
keep_count = 0
idx = 0
while(1):
    idx = idx + 1
    # get a frame
    ret, frame = cap.read()
    # show a frame
    parameters = findParameters_rem_outlier(frame,40)
##    plt.imshow(frame)
    plt.show()
    if parameters != 0:
        k,b = parameters
#        print(math.atan(k))
        if len(k_cache)==0: k_cache=[abs(math.atan(k)) for _ in range(0,CACHE_CONST)]
        if keep_count>MAX_KEEP_CONST:
            k_cache=[math.atan(k) for _ in range(0,CACHE_CONST)]
            keep_count=0
        elif abs(np.mean(k_cache)-math.atan(k))<delK_THRESHOLD or abs(np.mean(k_cache)+math.atan(k))<delK_THRESHOLD:
            k_cache.pop(0)
            k_cache.append(abs(math.atan(k)))
            line_p1 = (0,math.floor(b))
            line_p2 = (1000, math.floor(1000*k + b))
            cv2.line(frame,line_p1,line_p2,(255,0,0),2)



            
            last_k = k
            last_b = b
        else:
            print('break threshold')
            keep_count=keep_count+1
            k,b=last_k,last_b
            line_p1 = (0,math.floor(b))
            line_p2 = (1000, math.floor(1000*k + b))
            cv2.line(frame,line_p1,line_p2,(255,0,0),2)

        print([idx,math.atan(k)])
        table.putNumber('index',idx)
        table.putNumber('k',k)

        
    cv2.imshow("capture", frame)
    if cv2.waitKey(1) & 0xFF == ord('q'):
        break
cap.release()
cv2.destroyAllWindows() 





##最低点
##k
##
