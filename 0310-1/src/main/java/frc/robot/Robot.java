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
import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.*;
import frc.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
    public static Drivetrain m_drivetrain = null;
    public static OI m_oi;
    public static Shooter m_shooter = null;
    public static Solenoids m_solenoids = null;

    Command m_autonomousCommand;
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    public static DigitalOutput light = new DigitalOutput(0);

    public static int stretchStatus = 0;
    public static int cargoStatus = 0;
    public static int panelTakingStatus = 0;
    public static int speedStatus = 0;
    public static int traceStatus = 0;

    NetworkTableEntry stretchSEntry;
    NetworkTableEntry cargoSEntry;
    NetworkTableEntry panelTakingSEntry;
    NetworkTableEntry speedSEntry;


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
        m_chooser.setDefaultOption("Default Auto", new ExampleCommand());
        // chooser.addOption("My Auto", new MyAutoCommand());
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

        Robot.light.set(false);

        stretchStatus = 0;
        cargoStatus = 0;
        panelTakingStatus = 0;
        speedStatus = 0;
        m_solenoids.stretchIn();
        m_solenoids.cargoDown();
        m_solenoids.panelTakingOff();
        m_solenoids.speedDown();

        NetworkTableInstance inst = NetworkTableInstance.getDefault();
        NetworkTable table = inst.getTable("datatable");

        stretchSEntry = table.getEntry("Stretch Status");
        cargoSEntry = table.getEntry("Cargo Status");
        panelTakingSEntry = table.getEntry("Panel Taking Status");
        speedSEntry = table.getEntry("Speed Status");
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
        Buttonsss b1 = new Buttonsss();
        Thread threadB1 = new Thread(b1);
        threadB1.start();
        try {
            threadB1.sleep(200);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 

    }

    class Buttonsss implements Runnable {
        public void run() {
            //stretch
            if (m_oi.stretchButton.get() && stretchStatus == 0) {
                m_solenoids.stretchOut();
                stretchStatus = 1;
            } else if (m_oi.stretchButton.get() && stretchStatus == 1) {
                m_solenoids.stretchIn();
                stretchStatus = 0;
            }

            //cargo
            if (m_oi.cargoButton.get() && cargoStatus == 0) {
                m_solenoids.cargoUp();
                cargoStatus = 1;
            } else if (m_oi.cargoButton.get() && cargoStatus == 1) {
                m_solenoids.cargoDown();
                cargoStatus = 0;
            }

            //panel taking
            if (m_oi.panelTakingButton.get() && panelTakingStatus == 0) {
                m_solenoids.panelTakingOn();
                panelTakingStatus = 1;
            } else if (m_oi.panelTakingButton.get() && panelTakingStatus == 1) {
                m_solenoids.panelTakingOff();
                panelTakingStatus = 0;
            }

            //speed
            if(m_oi.speedButton.get()) {
                m_solenoids.speedUp();
                speedStatus = 1;
            } else {
                m_solenoids.speedDown();
                speedStatus = 0;
            }
            
            cargoSEntry.setDouble(cargoStatus);
            stretchSEntry.setDouble(stretchStatus);
            panelTakingSEntry.setDouble(panelTakingStatus);
            speedSEntry.setDouble(speedStatus);
        }
    }

    public boolean getButton(Button b) throws InterruptedException {
        Thread.sleep(200);
        return b.get();
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
