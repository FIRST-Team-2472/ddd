package frc.robot.commands;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Robot;
import frc.robot.generated.LimelightHelpers;
import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

public class AutoAim extends Command {

    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    //private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
   //         .withSteerRequestType(SteerRequestType.Position);
    private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
    private CommandSwerveDrivetrain drivetrain;

    double xOffset;
   boolean tv;
     
    public AutoAim(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    public double getXOffset() {
        double targetXOffset;
        if (!Robot.isSimulation()) {

            targetXOffset = LimelightHelpers.getTX("limelight-three");
        } else {
            targetXOffset = -15;
        }
        SmartDashboard.putNumber("target offset x", targetXOffset);
        return targetXOffset;

    }

    public Boolean getTV() {
        boolean tv = LimelightHelpers.getTV("limelight-three");
        return tv;
    }


    @Override
    public void initialize() {

    }

    @Override
    public void execute() {
        xOffset = getXOffset();
        tv = getTV();
        System.out.println("xOffset: " + xOffset);
        if (tv == false) {
            drivetrain.setControl(drive.withRotationalRate(0));
        } else {
            if (xOffset > 1) {
                System.out.println("Turning one way");
                drivetrain.setControl(drive.withRotationalRate((-0.1 * MaxAngularRate)));
            } else if (xOffset < -1) {
                System.out.println("Turning the other way");
                drivetrain.setControl(drive.withRotationalRate(0.1 * MaxAngularRate));
            }
        }
    }

    @Override
    public boolean isFinished() {

        if ((xOffset >= -1 && xOffset <= 1) && tv == true) {
         return true;
        }
        return false;
    }

    @Override
    public void end(boolean parameter) {
        drivetrain.setControl(drive.withRotationalRate(0));
    }
}
