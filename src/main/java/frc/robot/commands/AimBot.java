package frc.robot.commands;

import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.CommandSwerveDrivetrain;

import static edu.wpi.first.units.Units.RadiansPerSecond;

public class Aimbot extends Command {
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
            .withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withSteerRequestType(SteerRequestType.Position);


    public final CommandSwerveDrivetrain drivetrain;

    double xoffset;
    double sumError;
    double previousError;

    public Aimbot(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        sumError = 0;
        previousError = 0;
    }

    @Override
    public void execute() {
        xoffset = LimelightHelpers.getTX("limelight-three");
        if (!LimelightHelpers.getTV("limelight-three")) {
            return;
        }
        sumError += xoffset * 0.02;
        double kp = 0.1;
        double ki = 0;
        double kd = 0;
        double power = Math.min(1, Math.max(-1, (kp * xoffset) + (ki * sumError) + (xoffset - previousError)/0.02 * kd));

        drivetrain.setControl(m_driveRequest.withRotationalRate(power * MaxAngularRate));
        previousError = xoffset;
    }

    @Override
    public boolean isFinished() {
        return -1 <= xoffset && xoffset <= 1;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setControl(m_driveRequest.withRotationalRate(0.0));
    }
}