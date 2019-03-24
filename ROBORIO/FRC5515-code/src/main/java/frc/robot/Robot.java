/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSink;
import edu.wpi.cscore.CvSource;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    public static Drivetrain m_drivetrain = null;
    public static OI m_oi;
    public static Shooter m_shooter = null;
    public static Solenoids m_solenoids = null;

    Command m_autonomousCommand;
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    public static int fingerStatus = 0;
    public static int liftStatus = 0;
    public static int panelTakingStatus = 0;
    public static int speedStatus = 0;
    public static int stretchStatus = 0;
    public static int switchStatus = 0;

    public static NetworkTableEntry fingerSEntry;
    public static NetworkTableEntry liftSEntry;
    public static NetworkTableEntry panelTakingSEntry;
    public static NetworkTableEntry speedSEntry;
    public static NetworkTableEntry stretchSEntry;
    public static NetworkTableEntry switchSEntry;

    public static NetworkTableEntry leftSpeedEntry;
    public static NetworkTableEntry rightSpeedEntry;    

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        m_drivetrain = new Drivetrain();
        m_oi = new OI();
        m_shooter = new Shooter();
        m_solenoids = new Solenoids();
        m_chooser.setDefaultOption("Default Auto", null);
        //chooser.addOption("My Auto", new MyAutoCommand());
        SmartDashboard.putData("Auto mode", m_chooser);

        new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(640, 480);

            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 320, 240);

            Mat source = new Mat();
            Mat output = new Mat();

            while (!Thread.interrupted()) {
                cvSink.grabFrame(source);
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
        }).start();

        new Thread(() -> {
            UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
            camera.setResolution(640, 480);

            CvSink cvSink = CameraServer.getInstance().getVideo();
            CvSource outputStream = CameraServer.getInstance().putVideo("Blur", 320, 240);

            Mat source = new Mat();
            Mat output = new Mat();

            while (!Thread.interrupted()) {
                cvSink.grabFrame(source);
                Imgproc.cvtColor(source, output, Imgproc.COLOR_BGR2GRAY);
                outputStream.putFrame(output);
            }
        }).start();

        fingerStatus = 0;
        liftStatus = 0;        
        panelTakingStatus = 0;
        speedStatus = 0;
        stretchStatus = 0;
        switchStatus = 0;
        m_solenoids.fingerOff();
        m_solenoids.liftOff();
        m_solenoids.panelTakingOff();
        m_solenoids.speedOff();
        m_solenoids.stretchOff();
        m_solenoids.switch0Off();

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("datatable");

        fingerSEntry = table.getEntry("Finger Status");
        liftSEntry = table.getEntry("Lift Status");
        panelTakingSEntry = table.getEntry("Panel Taking Status");
        speedSEntry = table.getEntry("Speed Status");
        stretchSEntry = table.getEntry("Stretch Status");
        switchSEntry = table.getEntry("Switch Status");

        leftSpeedEntry = table.getEntry("Left Speed");
        rightSpeedEntry = table.getEntry("Right Speed");
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like diagnostics that you want ran during disabled, autonomous,
     * teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
    }

    /**
     * This function is called once each time the robot enters Disabled mode. You
     * can use it to reset any subsystem information you want to clear when the
     * robot is disabled.
     */
    @Override
    public void disabledInit() {
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.getInstance().run();
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable chooser
     * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
     * remove all of the chooser code and uncomment the getString code to get the
     * auto name from the text box below the Gyro
     *
     * <p>
     * You can add additional auto modes by adding additional commands to the
     * chooser code above (like the commented example) or additional comparisons to
     * the switch structure below with additional strings & commands.
     */
    @Override
    public void autonomousInit() {
        m_autonomousCommand = m_chooser.getSelected();

        /*
         * String autoSelected = SmartDashboard.getString("Auto Selector", "Default");
         * switch(autoSelected) { case "My Auto": autonomousCommand = new
         * MyAutoCommand(); break; case "Default Auto": default: autonomousCommand = new
         * ExampleCommand(); break; }
         */

        // schedule the autonomous command (example)
        if (m_autonomousCommand != null) {
            m_autonomousCommand.start();
        }
    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        Scheduler.getInstance().run();
    }

    

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
            
            
        }
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
        double leftSpeed = 0.3*Robot.m_oi.driveController1.getRawAxis(RobotMap.DRIVE_CONTROLLER_LEFT_TRIGGER_AXIS);
        double rightSpeed = -0.3*Robot.m_oi.driveController1.getRawAxis(RobotMap.DRIVE_CONTROLLER_RIGHT_TRIGGER_AXIS);
        leftSpeedEntry.setDouble(leftSpeed);
        rightSpeedEntry.setDouble(rightSpeed);
        m_drivetrain.tankDrive(leftSpeed, rightSpeed);
/*
        Buttonsss b1 = new Buttonsss();
        Thread threadB1 = new Thread(b1);
        threadB1.start();
        try {
            threadB1.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
        */
    }

    class Buttonsss implements Runnable {
        public void run() {
            /*
            //finger
            if(Robot.m_oi.fingerButton.get() && Robot.fingerStatus == 0) {
                Robot.m_solenoids.fingerOn();
                Robot.switchStatus = 1;
            } else if (Robot.m_oi.fingerButton.get() && Robot.fingerStatus == 1) {
                Robot.m_solenoids.fingerOff();
                Robot.fingerStatus = 0;
            }

            //lift
            if(Robot.m_oi.speedButton.get() && Robot.liftStatus == 0) {
                Robot.m_solenoids.liftOn();
                Robot.switchStatus = 1;
            } else if (Robot.m_oi.speedButton.get() && Robot.liftStatus == 1) {
                Robot.m_solenoids.liftOff();
                Robot.liftStatus = 0;
            }

            //panelTaking
            if(Robot.m_oi.panelTakingButton.get() && Robot.panelTakingStatus == 0) {
                Robot.m_solenoids.panelTakingOn();
                Robot.panelTakingStatus = 1;
            } else if (Robot.m_oi.panelTakingButton.get() && Robot.panelTakingStatus == 1) {
                Robot.m_solenoids.panelTakingOff();
                Robot.panelTakingStatus = 0;
            }

            //speed
            if(Robot.m_oi.speedButton.get() && Robot.speedStatus == 0) {
                Robot.m_solenoids.speedOn();
                Robot.switchStatus = 1;
            } else if (Robot.m_oi.speedButton.get() && Robot.speedStatus == 1) {
                Robot.m_solenoids.speedOff();
                Robot.speedStatus = 0;
            }

            //stretch
            if(Robot.m_oi.stretchButton.get() && Robot.stretchStatus == 0) {
                Robot.m_solenoids.stretchOn();
                Robot.stretchStatus = 1;
            } else if (Robot.m_oi.stretchButton.get() && Robot.stretchStatus == 1) {
                Robot.m_solenoids.stretchOff();
                Robot.stretchStatus = 0;
            }

            //switch0
            if(Robot.m_oi.switchButton.get() && Robot.switchStatus == 0) {
                Robot.m_solenoids.switch0On();
                Robot.switchStatus = 1;
            } else if (Robot.m_oi.switchButton.get() && Robot.switchStatus == 1) {
                Robot.m_solenoids.switch0Off();
                Robot.switchStatus = 0;

            }

            Robot.fingerSEntry.setDouble(Robot.fingerStatus);
            Robot.liftSEntry.setDouble(Robot.liftStatus);
            Robot.panelTakingSEntry.setDouble(Robot.panelTakingStatus);
            Robot.speedSEntry.setDouble(Robot.speedStatus);
            Robot.stretchSEntry.setDouble(Robot.stretchStatus);
            Robot.switchSEntry.setDouble(Robot.switchStatus);*/
        }
    }
    

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
