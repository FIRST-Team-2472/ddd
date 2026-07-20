package frc.robot.commands;

import static edu.wpi.first.units.Units.RotationsPerSecond;

import com.ctre.phoenix6.swerve.SwerveModule.SteerRequestType;
import com.ctre.phoenix6.swerve.SwerveRequest;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.LimelightHelpers;
import frc.robot.generated.TunerConstants;
import frc.robot.subsystems.CommandSwerveDrivetrain;

import com.ctre.phoenix6.hardware.Pigeon2;

import static edu.wpi.first.units.Units.RadiansPerSecond;

public class Aimbot extends Command {
    Timer timer = new Timer();
    private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond);
    private final Pigeon2 pigeon2 = new Pigeon2(0);

    private final SwerveRequest.FieldCentric m_driveRequest = new SwerveRequest.FieldCentric()
            .withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
            .withSteerRequestType(SteerRequestType.Position);

    public final CommandSwerveDrivetrain drivetrain = TunerConstants.createDrivetrain();

    double xoffset;
    double sumError;
    double previousError;

    public Aimbot() {
        addRequirements(drivetrain);
    }

    @Override
    public void initialize() {
        sumError = 0;
        previousError = 0;
        timer.restart();
    }

    @Override
    public void execute() {
        xoffset = LimelightHelpers.getTX("limelight");
        sumError += xoffset;
        double kp = 0;
        double ki = 0;
        double kd = 0;
        double power = (kp*xoffset) + (ki * sumError) + (xoffset - previousError)/timer.get() * kd;
    
        drivetrain.applyRequest(() -> m_driveRequest.withRotationalRate(0.2*power));
        previousError = xoffset;
    }

    @Override
    public boolean isFinished() {
        return -1 <= xoffset && xoffset <= 1;
    }

    @Override
    public void end(boolean interrupted) {
        drivetrain.applyRequest(() -> m_driveRequest.withRotationalRate(0.0));
    }
}