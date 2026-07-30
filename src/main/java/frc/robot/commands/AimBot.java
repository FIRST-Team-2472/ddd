package frc.robot.commands;

import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.math.controller.PIDController;
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
        SmartDashboard.putNumber("X Offset", xoffset);
        if (!LimelightHelpers.getTV("limelight-three")) {
            return;
        }
        PIDController pidController = new PIDController(0.05, 0, 0);
        double power = Math.max(-1, Math.min(1, pidController.calculate(xoffset, 0)));

        drivetrain.setControl(m_driveRequest.withRotationalRate(-power * MaxAngularRate));
        previousError = xoffset;
    }

    @Override
    public boolean isFinished() {
        return false /*-0.1 <= xoffset && xoffset <= 0.1*/;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.setControl(m_driveRequest.withRotationalRate(0.0));
    }
}