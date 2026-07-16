package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.LimelightHelpers;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

// Not sure if importing these is what I should be doing but they're stopping the errors so...
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;;

public class AimBot extends Command {

      private CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
      private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

      private double targetOffset;

      private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
            .withSteerRequestType(SteerRequestType.Position);

      public AimBot() {

      }

      @Override
      public void initialize() {

      }

      @Override
      public void execute() {
            targetOffset = LimelightHelpers.getTX("limelight");
            if (targetOffset > 1) {
                  drivetrain.applyRequest(() ->
                        m_driveRequest.withRotationalRate(-0.2 * MaxAngularRate));
            } else if (targetOffset < -1) {
                  drivetrain.applyRequest(() ->
                        m_driveRequest.withRotationalRate(0.2 * MaxAngularRate));
            }
      }

      @Override
      public boolean isFinished() {
            return -1 <= targetOffset && targetOffset <= 1;
      }

      @Override
      public void end(boolean parameter) {
            drivetrain.applyRequest(() ->
            m_driveRequest.withRotationalRate(0));
      }
}