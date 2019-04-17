/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

//import edu.wpi.first.wpilibj.DoubleSolenoid;
//import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.RobotMap;

/**
 * Add your docs here.
 */
public class Solenoids extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public Solenoid finger = null;
    public Solenoid lift = null;
    public Solenoid panelTaking = null;
    public Solenoid speed = null;
    public Solenoid stretch = null;
    public Solenoid switch0 = null;
    //public DoubleSolenoid switch0 = null;

    public Solenoids() {
        finger = new Solenoid(RobotMap.SOLENOIDS_FINGER_SOLENOID);
       /* lift = new Solenoid(RobotMap.SOLENOIDS_LIFT_SOLENOID);
        panelTaking = new Solenoid(RobotMap.SOLENOIDS_PANELTAKING_SOLENOID);
        speed = new Solenoid(RobotMap.SOLENOIDS_SPEED_SOLENOID);
        stretch = new Solenoid(RobotMap.SOLENOIDS_STRETCH_SOLENOID);
        switch0 = new Solenoid(RobotMap.SOLENOIDS_SWITCH0_SOLENOID);*/
        //switch0 = new DoubleSolenoid(RobotMap.SOLENOIDS_SWITCH1_SOLENOID_DEPLOY, RobotMap.SOLENOIDS_SWITCH1_SOLENOID_RETRACT);
    }

    public void fingerOn() {
        finger.set(true);
    }

    public void fingerOff() {
        finger.set(false);
    }

    public void liftOn() {
        lift.set(true);
    }

    public void liftOff() {
        lift.set(false);
    }

    public void panelTakingOn() {
        panelTaking.set(true);
    }

    public void panelTakingOff() {
        panelTaking.set(false);
    }
    
    public void speedOn() {
        speed.set(true);
    }

    public void speedOff() {
        speed.set(false);
    }

    public void stretchOn() {
        stretch.set(true);
    }

    public void stretchOff() {
        stretch.set(false);
    }

    public void switch0On() {
        switch0.set(true);
    }

    public void switch0Off() {
        switch0.set(false);
    }
/*
    public void switch0On() {
        switch0.set(Value.kReverse);
    }

    public void switch0Off() {
        switch0.set(Value.kForward);
    }
*/

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
    }
}
