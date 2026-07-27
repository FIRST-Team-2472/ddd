package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.LimelightHelpers;
import frc.robot.subsystems.CommandSwerveDrivetrain;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;;

public class AimBot extends Command {

      private CommandSwerveDrivetrain drivetrain;
      private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

      private double targetOffset;
      private boolean targetVisible;

      private double kP = -0.03;

      private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

      public AimBot(CommandSwerveDrivetrain drivetrain) {
            this.drivetrain = drivetrain;
            addRequirements(drivetrain);
      }

      @Override
      public void initialize() {

      }

      @Override
      public void execute() {
            System.out.println("Executed aimbot");
            targetVisible = LimelightHelpers.getTV("limelight-three");
            if (targetVisible) {
                  System.out.println("target is visible");
                  targetOffset = LimelightHelpers.getTX("limelight-three");
                  if (targetOffset > 1) {
                        System.out.println("turning right");
                        drivetrain.setControl(
                              m_driveRequest.withRotationalRate(Math.max(kP * targetOffset * MaxAngularRate, -0.5)));
                  } else if (targetOffset < -1) {
                        System.out.println("turning left");
                        drivetrain.setControl(
                              m_driveRequest.withRotationalRate(Math.min(kP * targetOffset * MaxAngularRate, 0.5)));
                  }
            } else {
                  drivetrain.setControl(
                        m_driveRequest.withRotationalRate(0));
            }
      }

      @Override
      public boolean isFinished() {
            return targetVisible && -1 <= targetOffset && targetOffset <= 1;
      }

      @Override
      public void end(boolean parameter) {
            drivetrain.setControl(
                  m_driveRequest.withRotationalRate(0));
      }
}