/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.RobotMap;
import frc.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
    public Joystick driveController = new Joystick(RobotMap.OI_DRIVER_CONTROLLER);
    public Button traceButton = new JoystickButton(driveController, 4);
    public Button stretchButton = new JoystickButton(driveController, 3);
    public Button cargoButton = new JoystickButton(driveController, 2);
    public Button panelTakingButton = new JoystickButton(driveController, 1);
    public Button speedButton = new JoystickButton(driveController, 9);

    public OI() {
        traceButton.whileHeld(new TraceTarget());
        //panelTakingButton.whenPressed(new PanelTaking());
        //stretchButton.whenPressed(new StretchOut());
    }
}
