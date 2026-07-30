package frc.robot.commands;

import static edu.wpi.first.units.Units.RadiansPerSecond;
import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.subsystems.CommandSwerveDrivetrain;

public class Aimbot extends Command {
    private final double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);

    private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
            .withDriveRequestType(DriveRequestType.OpenLoopVoltage);

    public final CommandSwerveDrivetrain drivetrain;

    // Optional WPILib PIDController instance if you prefer to use WPILib's controller:
    // private final PIDController pid = new PIDController(0.025, 0.0, 0.002);

    double xoffset;
    double previousError;

    public Aimbot(CommandSwerveDrivetrain drivetrain) {
        this.drivetrain = drivetrain;
        addRequirements(drivetrain);

        // If using WPILib PIDController:
        // pid.setSetpoint(0.0);
        // pid.setTolerance(1.0);
    }

    @Override
    public void initialize() {
        previousError = 0;
        // If using WPILib PIDController:
        // pid.reset();
    }

    @Override
    public void execute() {
        xoffset = LimelightHelpers.getTX("limelight-three");
        SmartDashboard.putNumber("X Offset", xoffset);

        // If Limelight loses sight of target, bring rotation rate to zero safely
        if (!LimelightHelpers.getTV("limelight-three")) {
            drivetrain.setControl(m_driveRequest.withRotationalRate(0));
            return;
        }

        // --- METHOD 1: Working Manual PD Calculation ---
        double kp = 0.025;
        double kd = 0.002;
        double power = Math.min(1, Math.max(-1, (kp * xoffset) + (xoffset - previousError) / 0.02 * kd));

        // Note on Sign: (kp * xoffset) is positive when target is right (tx > 0).
        // To turn right (clockwise), WPILib requires a negative rotational rate, hence -power.
        drivetrain.setControl(m_driveRequest.withRotationalRate(-power * MaxAngularRate));

        /* 
         * --- METHOD 2: How to use WPILib's PIDController correctly ---
         * 
         * Why it spun out before:
         * pid.calculate(xoffset, 0.0) computes (0.0 - xoffset) * kp, which is ALREADY NEGATIVE for tx > 0.
         * If you put a '-' in front of pid.calculate(), it double-negates the output to positive,
         * commanding a LEFT turn when target is RIGHT (positive feedback -> runaway spin!).
         * 
         * To use PIDController properly, pass its output WITHOUT a minus sign:
         * 
         * double pidPower = pid.calculate(xoffset, 0.0); // Already negative for positive tx (target right)
         * double rotationalRate = MathUtil.clamp(pidPower * MaxAngularRate, -MaxAngularRate, MaxAngularRate);
         * drivetrain.setControl(m_driveRequest.withRotationalRate(rotationalRate));
         */

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