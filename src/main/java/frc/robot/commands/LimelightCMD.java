package frc.robot.commands;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.Command;

public class LimelightCMD extends Command{
    private NetworkTable table;
    public LimelightCMD() {
        table = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public boolean readTelemetry() {
        NetworkTableEntry txEntry = table.getEntry("tx");
        NetworkTableEntry tvEntry = table.getEntry("tv");
        NetworkTableEntry taEntry = table.getEntry("ta");


        double tx = txEntry.getDouble(0.0);
        boolean targetVisible = tvEntry.getDouble(0.0) == 1.0;
        double targetAreaPCT = taEntry.getDouble(0.0);

         if (targetVisible) {
            System.out.println("Offset (degrees): " + tx);
        } else {
            System.out.println("Target not found");
        }
        return targetVisible;

    }
    public boolean isTargetLocked() {
        NetworkTableEntry tvEntry = table.getEntry("tv");
        NetworkTableEntry taEntry = table.getEntry("ta");

        boolean targetVisible = tvEntry.getDouble(0.0) == 1.0;
        double targetAreaPCT = taEntry.getDouble(0.0);

        boolean isTargetLocked;

        if (targetVisible && (targetAreaPCT > 0.05)) {
            isTargetLocked = true;
        } else {
            isTargetLocked = false;
        }

        return isTargetLocked;
    }

}
