package frc.robot.commands;

import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.CommandSwerveDrivetrain;

import static edu.wpi.first.units.Units.RadiansPerSecond;

public class Aimbot extends Command {
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);


    public final CommandSwerveDrivetrain drivetrain;

    double xoffset;
    double previousError;

    public Aimbot(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        previousError = 0;
    }

    @Override
    public void execute() {
        xoffset = LimelightHelpers.getTX("limelight-three");
        SmartDashboard.putNumber("X Offset", xoffset);
        if (!LimelightHelpers.getTV("limelight-three")) {
            drivetrain.setControl(m_driveRequest.withRotationalRate(0));
            return;
        }
        double kp = 0.025;
        double kd = 0.002;
        double power = Math.min(1, Math.max(-1, (kp * xoffset) + (xoffset - previousError)/0.02 * kd));

        drivetrain.setControl(m_driveRequest.withRotationalRate(-power * MaxAngularRate));
        previousError = xoffset;
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setControl(m_driveRequest.withRotationalRate(0.0));
    }
}