import math





del_x_1 = 50
del_theta_1 = 0.1

del_x_2 = 50
del_theta_2 = 0.1

MAX_MOTOR = 1
MIN_MOTOR = -1


motors = [0, 0]


def main_loop(del_x, theta):
    global motors
    motors = [0, 0]
    if del_x < del_x_1:  # d是不是对了
        if theta < del_theta_2:  # 角度是不是准
            run_2(del_x)  # 怼上去
        else:
            rotate_2(theta)  # 把角度调对
    else:
        if theta < del_theta_1:
            rotate_1(theta)  # 扩大角度，减少d更快
        else:
            run_1(del_x)  # 把d减少

    set_motors()

    def run_1(del_x):
        run_val = mapping(-100, 100, MIN_MOTOR, MAX_MOTOR, del_x)

        motors[0] += run_val
        motors[1] += run_val

        return

    def run_2(del_x):
        run_val = mapping(-100, 100, MIN_MOTOR, MAX_MOTOR, del_x)

        motors[0] += run_val
        motors[1] += run_val

        return run_val

    def rotate_1(theta):
        run_val = mapping(-math.pi/4, math.pi/4, MIN_MOTOR,
                          MAX_MOTOR, math.pi/4-theta)

        motors[0] -= run_val
        motors[1] += run_val

        return run_val

    def rotate_2(theta):
        run_val = mapping(-math.pi/4, math.pi/4, MIN_MOTOR, MAX_MOTOR, theta)

        motors[0] -= run_val
        motors[1] += run_val

        return

    def set_motors():
        
        
        #set motors
        return

    def mapping(dMIN, dMAX, vMIN, vMAX, d):
    # range: [-1, 1]
    # domain: [-1,1]

        d = dMIN if d < dMIN else d
        d = dMAX if d > dMAX else d

        x = 2/(dMAX-dMIN)*(d-dMIN)-1

        x = 1 if x > 1 else x
        x = -1 if x < -1 else x

        y = -1/4*math.atan(x)-4/5*x**3

        return (vMAX-vMIN)/2*y+dMIN
    return
