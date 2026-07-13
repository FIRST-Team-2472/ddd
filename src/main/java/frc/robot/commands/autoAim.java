package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.generated.LimelightHelpers;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

public class autoAim extends Command{

    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    //Pose2d pose = new Pose2d(1, 1, new Rotation2d());

    private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
      .withSteerRequestType(SteerRequestType.Position);
    private CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();


    public autoAim(){

    }
    
    public double getXOffset() {
        double targetXOffset = LimelightHelpers.getTX("limelight");
        return targetXOffset;
    }
    

    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        if (getXOffset() > 1) {
          drivetrain.applyRequest(() ->
         m_driveRequest.withRotationalRate(-0.2 * MaxAngularRate));  
        } else if (getXOffset() < -1) {
        drivetrain.applyRequest(() ->
         m_driveRequest.withRotationalRate(0.2 * MaxAngularRate));
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean parameter) {

    }
}