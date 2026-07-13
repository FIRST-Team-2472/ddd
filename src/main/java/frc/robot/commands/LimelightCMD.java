package frc.robot.commands;

import edu.wpi.first.networktables.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;

public class LimelightCMD extends Command {
    private NetworkTable table;

    public LimelightCMD() {
        // Constructor
    }

    @Override
    public void initialize() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    @Override
    public void execute() {
        readTelemetry();
    }

    public void readTelemetry() {
        double tx = table.getEntry("tx").getDouble(0.0);
        double ty = table.getEntry("ty").getDouble(0.0);
        double ta = table.getEntry("ta").getDouble(0.0);

        System.out.println("Limelight Telemetry:");
        System.out.println("tx: " + tx);
        System.out.println("ty: " + ty);
        System.out.println("ta: " + ta);

        boolean targetVisible = table.getEntry("tv").getDouble(0.0) == 1.0;

        boolean targetLocked = targetVisible && (ta >= 5.0);

        if (targetVisible) {
            SmartDashboard.putNumber("Target Found! Degrees offset: ", tx);
        } else {
            SmartDashboard.putNumber("Searching for target...", 0);
        }
        SmartDashboard.putBoolean("Target Locked", targetLocked);
    }

    @Override
    public boolean isFinished() {
        return false; // This command never finishes on its own
    }
}
