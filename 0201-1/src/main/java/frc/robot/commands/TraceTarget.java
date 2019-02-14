/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.vision.VisionThread;
import edu.wpi.first.wpilibj.Sendable;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.MyVisionPipeline;
import frc.robot.Robot;
import frc.robot.subsystems.Drivetrain;

public class TraceTarget extends Command {
    private static final int IMG_WIDTH = 320;
    private static final int IMG_HEIGHT = 240;

    private VisionThread visionThread;
    private double centerX = 0.0;
    private TankDrive tankDrive;

    private final Object imgLock = new Object();

    NetworkTableEntry xEntry;
    NetworkTableEntry yEntry;

    public TraceTarget() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(Robot.m_drivetrain);
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {

     NetworkTableInstance inst = NetworkTableInstance.getDefault();
     NetworkTable table = inst.getTable("datatable");
     xEntry = table.getEntry("X");
     yEntry = table.getEntry("Y");


        UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
        camera.setResolution(IMG_WIDTH, IMG_HEIGHT);

       

        visionThread = new VisionThread(camera, new MyVisionPipeline(), pipeline -> {
          if (!pipeline.findContoursOutput().isEmpty()) {
            Rect r = Imgproc.boundingRect(pipeline.findContoursOutput().get(0));
            synchronized (imgLock) {
                centerX = r.x + (r.width / 2);
            }
        }
            
           
        });
        visionThread.start();

        tankDrive = new TankDrive();
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
        double centerX;
        synchronized (imgLock) {
            centerX = this.centerX;
        }
        double turn = centerX - (IMG_WIDTH / 2);
        xEntry.setDouble(centerX);
        yEntry.setDouble(233.233);

        if(centerX > 50) {
          Robot.m_drivetrain.tankDrive(0, 0.05*centerX/50);
        }
        else{
          Robot.m_drivetrain.tankDrive(0, -0.05*50/centerX);
        }

    //    Robot.m_drivetrain.tankDrive(-0.6, turn * 0.005);

     }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
        end();
    }
}
