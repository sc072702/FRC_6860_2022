/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;


import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.Spark;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.XboxController;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */

public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  SlewRateLimiter filter = new SlewRateLimiter(0.05);


  // defining Drivetrain variables
private final Spark RightMotorFront = new Spark(1); // right motor set to 1st pwm
private final Spark RightMotorback = new Spark(2); // right motor set 2nd pwm

private final Spark LeftMotorFront = new Spark(3); // left motor set to 3rd pwm
private final Spark LeftMotorBack = new Spark(4); // left motor set to 4th pwm



//slew rate limiter


// Variable groups
private final MotorControllerGroup RightDrive = new MotorControllerGroup(RightMotorFront, RightMotorback); // set of right motors
private final MotorControllerGroup LeftDrive = new MotorControllerGroup(LeftMotorFront, LeftMotorBack); // set of left motors

//Drive train
DifferentialDrive DriveTrain = new DifferentialDrive(RightDrive, LeftDrive); // combined motors

// Intake Motors
private final Spark PrimaryIntk = new Spark(0); // first intake  set to pwm slot 0
private final Spark SecondaryIntk = new Spark(6);

 // Conveyor set to 6th pwm


  



// Climb Motors
private final Spark Climb = new Spark(5); // climb   set to 0 pwm

// Controller (Xbox)
XboxController Controller_1 = new XboxController(0); // controller (# = port)

 
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

   
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
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
    // m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  /*
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
        //If (Controller_1.getBButton())
         //stop command
        
          
          
        

      case kCustomAuto:
        // Put custom auto code here
        //double time = Timer.getFPGATimestamp();
      long t= System.currentTimeMillis();
      long end= t+3000;

        while (System.currentTimeMillis()< end) {
        RightMotorFront.set(0.10);
        RightMotorback.set(0.10);
        LeftMotorFront.set(0.10);
        LeftMotorBack.set(0.10);
      
          if (Controller_1.getBButton()){
            break;
          }
        }
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }
 */
  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    double motorSpeed = 0.03;
    RightMotorFront.set(motorSpeed);
    RightMotorback.set(motorSpeed);
    LeftMotorFront.set(motorSpeed);
    LeftMotorBack.set(motorSpeed);
    filter.calculate(Controller_1.getLeftX());
    filter.calculate(Controller_1.getRightY());
    DriveTrain.arcadeDrive(Controller_1.getLeftX(), Controller_1.getRightY());

       
   if (Controller_1.getBButton()) {    // sets primary intake to forward
    PrimaryIntk.set(0.5);}
    else {PrimaryIntk.set(0);}
  
    /* Intake Reverse (WIP)
   if (Controller_1.getRightBumper()) {
    PrimaryIntk.set(-.5);}
   else {PrimaryIntk.set(0);}
   */

    // Secondary Intake
    if (Controller_1.getYButton()) {SecondaryIntk.set(-0.60);}
    else {SecondaryIntk.set(0);}

    //climb
    if (Controller_1.getXButton()) {
      Climb.set(0.8);
    }
    else if (Controller_1.getAButton()) {
      Climb.set(-0.6);
    }
    else {
      Climb.stopMotor();
    }
    
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
