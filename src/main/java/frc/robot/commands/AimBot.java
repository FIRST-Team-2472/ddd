package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.LimelightHelpers;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

//import static edu.wpi.first.units.Units.RadiansPerSecond;
//import static edu.wpi.first.units.Units.RotationsPerSecond;;

public class AimBot extends Command {

      private CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();
      //private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

      private double targetOffset;
      private boolean targetVisible;

      // kP is constant but I'm too lazy to make a constants file just for kP
      private double kP = 0.03;

      private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
            .withSteerRequestType(SteerRequestType.Position);

      public AimBot() {
            addRequirements(drivetrain);
      }

      @Override
      public void initialize() {

      }

      @Override
      public void execute() {
            targetVisible = LimelightHelpers.getTV("limelight");
            if (targetVisible) {
                  targetOffset = LimelightHelpers.getTX("limelight");
                  if (targetOffset > 1) {
                        drivetrain.applyRequest(() ->
                              m_driveRequest.withRotationalRate(Math.max(-kP * targetOffset, -0.5)));
                  } else if (targetOffset < -1) {
                        drivetrain.applyRequest(() ->
                              m_driveRequest.withRotationalRate(Math.min(kP * targetOffset, 0.5)));
                  }
            }
      }

      @Override
      public boolean isFinished() {
            return targetVisible && -1 <= targetOffset && targetOffset <= 1;
      }

      @Override
      public void end(boolean parameter) {
            drivetrain.applyRequest(() ->
                  m_driveRequest.withRotationalRate(0));
      }
}