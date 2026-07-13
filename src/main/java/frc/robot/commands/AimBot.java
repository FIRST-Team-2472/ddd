package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.LimelightHelpers;

private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond);
private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

public class AimBot extends Command {

      //vvvvvv Ignoring this for now vvvvvv

    private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
   .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
   .withDriveRequestType(DriveRequestType.OpenLoopVoltage)
   .withSteerRequestType(SteerRequestType.Position);

   drivetrain.applyRequest(() ->
         m_driveRequest.withVelocityX(-joystick.getLeftY() * MaxSpeed)
            .withVelocityY(-joystick.getLeftX() * MaxSpeed)
            .withRotationalRate(-joystick.getRightX() * MaxAngularRate)
      )

      public AimBot() {

      }

      @Override
      public void initialize() {

      }

      @Override
      public void execute() {
            double tx = LimelightHelpers.getTX("limelight");
            if (tx > 0) {
                  //turn right
            } 
            else if (tx < 0) {
                  //turn left
            }
      }

      @Override
      public boolean isFinished() {
            return false;
      }
}
