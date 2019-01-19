/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team5515.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.Encoder;

import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends IterativeRobot {
	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private SendableChooser<String> m_chooser = new SendableChooser<>();
	private Solenoid intakeSolenoid = new Solenoid(1);

	private Joystick Oscar = new Joystick(0);
	private Joystick Myra = new Joystick(1);

	private WPI_TalonSRX elevator = new WPI_TalonSRX(5);
	
	private WPI_TalonSRX leftmaster = new WPI_TalonSRX(2); //leftmaster
	private WPI_TalonSRX leftmaster1 = new WPI_TalonSRX(8);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                
	private WPI_TalonSRX rightmaster = new WPI_TalonSRX(3); //rightmaster
	
	//private WPI_TalonSRX rightmaster1 = new WPI_TalonSRX(7);
	//private DifferentialDrive BP19=new DifferentialDrive(leftmaster,rightmaster);
	//private DifferentialDrive BP20=new DifferentialDrive(leftmaster1,rightmaster1);
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		m_chooser.addDefault("Default Auto", kDefaultAuto);
		m_chooser.addObject("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);
		
		/* get the decoded pulse width encoder position, 4096 units per rotation 
		int pulseWidthPos = rightmaster.getSensorCollection().getPulseWidthPosition(); 
		/* get the pulse width in us, rise-to-fall in microseconds 
		int pulseWidthUs = rightmaster.getSensorCollection().getPulseWidthRiseToFallUs(); 
		/* get the period in us, rise-to-rise in microseconds 
		int periodUs = rightmaster.getSensorCollection().getPulseWidthRiseToRiseUs(); 
		/* get measured velocity in units per 100ms, 4096 units is one rotation 
		int pulseWidthVel = rightmaster.getSensorCollection().getPulseWidthVelocity(); 
		/* is sensor plugged in to Talon
		boolean sensorPluggedIn = false; 
		if (periodUs != 0) { 
			sensorPluggedIn = true;
		}
		int pulseWidthPoss = rightmaster1.getSensorCollection().getPulseWidthPosition(); 
	 	get the pulse width in us, rise-to-fall in microseconds 
		int pulseWidthUss = rightmaster1.getSensorCollection().getPulseWidthRiseToFallUs(); 
		 get the period in us, rise-to-rise in microseconds 
		int periodUss = rightmaster1.getSensorCollection().getPulseWidthRiseToRiseUs(); 
		 get measured velocity in units per 100ms, 4096 units is one rotation 
		int pulseWidthVell = rightmaster1.getSensorCollection().getPulseWidthVelocity(); 
		is sensor plugged in to Talon 
		boolean sensorPluggedInn = false; 
		if (periodUs != 0) { 
			sensorPluggedInn = true;
		}
		*/
		rightmaster.set(ControlMode.PercentOutput, 0);
		//rightmaster1.set(ControlMode.PercentOutput,0);
		rightmaster.setInverted(true);
		//rightmaster1.setInverted(true);

		
		elevator.set(ControlMode.PercentOutput, 0);
		elevator.setInverted(true);
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to
	 * the switch structure below with additional strings. If using the
	 * SendableChooser make sure to add them to the chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		switch (m_autoSelected) {
			case kCustomAuto:
				// Put custom auto code here
				break;
			case kDefaultAuto:
			default:
				// Put default auto code here
				break;
		}
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		if(Myra.getRawButton(7)) {
		double turn=Oscar.getRawAxis(0);
		double forward=Oscar.getRawAxis(1);
		//BP19.arcadeDrive(-0.9*forward,0.8*turn);
		//BP20.arcadeDrive(-0.9*forward,0.8*turn);
		}
		else if(Myra.getRawButton(8)) {
			double turn=Oscar.getRawAxis(0);
			double forward=Oscar.getRawAxis(1);
			//BP19.arcadeDrive(0.9*forward,-0.8*turn);
			//BP20.arcadeDrive(0.9*forward,-0.8*turn);
		}
		else{
			double turn=Oscar.getRawAxis(0);
			double forward=Oscar.getRawAxis(1);
			//BP19.arcadeDrive(-0.9*forward,0.8*turn);
			//BP20.arcadeDrive(-0.9*forward,0.8*turn);
			}
		if(Oscar.getRawButton(2)) {
		intakeSolenoid.set(true);
		}
		if(Oscar.getRawButton(1)) {
			intakeSolenoid.set(false);
		}
		//double elevator=Myra.getRawAxis(1);
		
		
		
		
		
		/*if (Oscar.getRawButton(7)) {
			elevator.set(1);
		}
		if(Myra.getRawButton(4)) {
			elevator.set(-0.5);
		}*/
		if(Oscar.getRawButton(12)) {
		rightmaster.set(0.3);
		rightmaster.configForwardSoftLimitThreshold(30*4096, 10);/* +14 rotations forward when using CTRE Mag encoder */ 
		rightmaster.configForwardSoftLimitEnable(true, 10); 
		rightmaster.overrideLimitSwitchesEnable(true);
		}
		else {
			rightmaster.set(0.3);
			rightmaster.configForwardSoftLimitThreshold(0*4096, 10);/* +14 rotations forward when using CTRE Mag encoder */ 
			rightmaster.configForwardSoftLimitEnable(true, 10); 
			rightmaster.overrideLimitSwitchesEnable(true);
			}
		//rightmaster1.configForwardSoftLimitThreshold(+14*4096, 10);/* +14 rotations forward when using CTRE Mag encoder */ 
		//rightmaster1.configForwardSoftLimitEnable(true, 10); 
		//rightmaster1.overrideLimitSwitchesEnable(true);
		
		}
	


	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}
