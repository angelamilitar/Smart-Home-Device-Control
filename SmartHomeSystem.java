// Command Interface
interface Command {
    void execute();
    void undo();
}

// Smart Device Interface
interface SmartDevice {
    void turnOn();
    void turnOff();
    String getStatus();
}

// Light Device
class Light implements SmartDevice {
    private String location;
    private boolean isOn;
    private int brightness;
    
    public Light(String location) {
        this.location = location;
        this.isOn = false;
        this.brightness = 0;
    }
    
    public void turnOn() {
        isOn = true;
        brightness = 75;
        System.out.println(location + " light is ON (Brightness: " + brightness + "%)");
    }
    
    public void turnOff() {
        isOn = false;
        brightness = 0;
        System.out.println(location + " light is OFF");
    }
    
    public String getStatus() {
        return location + " Light: " + (isOn ? "ON (Brightness: " + brightness + "%)" : "OFF");
    }
}

// Music Player Device
class MusicPlayer implements SmartDevice {
    private String location;
    private boolean isPlaying;
    private int volume;
    private String playlist;
    
    public MusicPlayer(String location) {
        this.location = location;
        this.isPlaying = false;
        this.volume = 50;
        this.playlist = "Default Playlist";
    }
    
    public void turnOn() {
        isPlaying = true;
        System.out.println(location + " music player is ON - Playing: " + playlist + " (Volume: " + volume + ")");
    }
    
    public void turnOff() {
        isPlaying = false;
        System.out.println(location + " music player is OFF");
    }
    
    public String getStatus() {
        return location + " Music: " + (isPlaying ? "PLAYING " + playlist + " (Vol: " + volume + ")" : "STOPPED");
    }
}

// Thermostat Device
class Thermostat implements SmartDevice {
    private String location;
    private boolean isOn;
    private int temperature;
    
    public Thermostat(String location, int temp) {
        this.location = location;
        this.isOn = false;
        this.temperature = temp;
    }
    
    public void turnOn() {
        isOn = true;
        System.out.println(location + " thermostat is ON - Set to " + temperature + "°C");
    }
    
    public void turnOff() {
        isOn = false;
        System.out.println(location + " thermostat is OFF");
    }
    
    public void increaseTemp() {
        if (isOn && temperature < 30) {
            temperature++;
            System.out.println(location + " temperature increased to " + temperature + "°C");
        }
    }
    
    public void decreaseTemp() {
        if (isOn && temperature > 15) {
            temperature--;
            System.out.println(location + " temperature decreased to " + temperature + "°C");
        }
    }
    
    public String getStatus() {
        return location + " Thermostat: " + (isOn ? "ON - " + temperature + "°C" : "OFF");
    }
}

// Light Commands
class LightOnCommand implements Command {
    private Light light;
    
    public LightOnCommand(Light light) {
        this.light = light;
    }
    
    public void execute() {
        light.turnOn();
    }
    
    public void undo() {
        light.turnOff();
    }
}

class LightOffCommand implements Command {
    private Light light;
    
    public LightOffCommand(Light light) {
        this.light = light;
    }
    
    public void execute() {
        light.turnOff();
    }
    
    public void undo() {
        light.turnOn();
    }
}

// Music Commands
class MusicOnCommand implements Command {
    private MusicPlayer music;
    
    public MusicOnCommand(MusicPlayer music) {
        this.music = music;
    }
    
    public void execute() {
        music.turnOn();
    }
    
    public void undo() {
        music.turnOff();
    }
}

class MusicOffCommand implements Command {
    private MusicPlayer music;
    
    public MusicOffCommand(MusicPlayer music) {
        this.music = music;
    }
    
    public void execute() {
        music.turnOff();
    }
    
    public void undo() {
        music.turnOn();
    }
}

// Thermostat Commands
class ThermostatUpCommand implements Command {
    private Thermostat thermostat;
    
    public ThermostatUpCommand(Thermostat thermostat) {
        this.thermostat = thermostat;
    }
    
    public void execute() {
        thermostat.increaseTemp();
    }
    
    public void undo() {
        thermostat.decreaseTemp();
    }
}

class ThermostatDownCommand implements Command {
    private Thermostat thermostat;
    
    public ThermostatDownCommand(Thermostat thermostat) {
        this.thermostat = thermostat;
    }
    
    public void execute() {
        thermostat.decreaseTemp();
    }
    
    public void undo() {
        thermostat.increaseTemp();
    }
}

// No Command (Null Object Pattern)
class NoCommand implements Command {
    public void execute() {}
    public void undo() {}
}

// Smart Home Hub (Central Controller)
class SmartHomeHub {
    private Command[] onCommands;
    private Command[] offCommands;
    private Command lastCommand;
    
    public SmartHomeHub() {
        onCommands = new Command[7];
        offCommands = new Command[7];
        
        Command noCommand = new NoCommand();
        for (int i = 0; i < 7; i++) {
            onCommands[i] = noCommand;
            offCommands[i] = noCommand;
        }
        lastCommand = noCommand;
    }
    
    public void setCommand(int slot, Command onCmd, Command offCmd) {
        if (slot >= 0 && slot < 7) {
            onCommands[slot] = onCmd;
            offCommands[slot] = offCmd;
        }
    }
    
    public void executeCommand(int slot) {
        if (slot >= 0 && slot < 7) {
            onCommands[slot].execute();
            lastCommand = onCommands[slot];
        }
    }
    
    public void executeOffCommand(int slot) {
        if (slot >= 0 && slot < 7) {
            offCommands[slot].execute();
            lastCommand = offCommands[slot];
        }
    }
    
    public void undoCommand() {
        lastCommand.undo();
    }
    
    public void showStatus() {
        System.out.println("\n=== Smart Home Hub Status ===");
        for (int i = 0; i < 7; i++) {
            System.out.println("Slot " + i + ": " + onCommands[i].getClass().getSimpleName() + " | " + offCommands[i].getClass().getSimpleName());
        }
        System.out.println("Last Command: " + lastCommand.getClass().getSimpleName());
        System.out.println("=============================\n");
    }
}

// Main Application
public class SmartHomeSystem {
    public static void main(String[] args) {
        System.out.println("🏠 === Smart Home Automation System === 🏠\n");
        
        // Create Smart Home Hub
        SmartHomeHub hub = new SmartHomeHub();
        
        // Create Devices
        Light livingRoom = new Light("Living Room");
        Light bedroom = new Light("Bedroom");
        MusicPlayer speaker = new MusicPlayer("Main Speaker");
        Thermostat thermostat = new Thermostat("Main Floor", 22);
        
        // Create Commands
        LightOnCommand livingRoomOn = new LightOnCommand(livingRoom);
        LightOffCommand livingRoomOff = new LightOffCommand(livingRoom);
        
        LightOnCommand bedroomOn = new LightOnCommand(bedroom);
        LightOffCommand bedroomOff = new LightOffCommand(bedroom);
        
        MusicOnCommand musicOn = new MusicOnCommand(speaker);
        MusicOffCommand musicOff = new MusicOffCommand(speaker);
        
        ThermostatUpCommand tempUp = new ThermostatUpCommand(thermostat);
        ThermostatDownCommand tempDown = new ThermostatDownCommand(thermostat);
        
        // Configure Hub
        hub.setCommand(0, livingRoomOn, livingRoomOff);
        hub.setCommand(1, bedroomOn, bedroomOff);
        hub.setCommand(2, musicOn, musicOff);
        hub.setCommand(3, tempUp, tempDown);
        
        // Show Initial Status
        hub.showStatus();
        
        // === DEMO TIME! ===
        System.out.println("🎮 === Starting Smart Home Demo === 🎮\n");
        
        // Turn on devices
        System.out.println("1️⃣ Turning on Living Room Light:");
        hub.executeCommand(0);
        
        System.out.println("\n2️⃣ Turning on Bedroom Light:");
        hub.executeCommand(1);
        
        System.out.println("\n3️⃣ Turning on Music:");
        hub.executeCommand(2);
        
        System.out.println("\n4️⃣ Turn on Thermostat and increase temperature:");
        thermostat.turnOn();
        hub.executeCommand(3);
        hub.executeCommand(3); // Increase again
        
        // Status Report
        System.out.println("\n📊 === Device Status Report === 📊");
        System.out.println("✅ " + livingRoom.getStatus());
        System.out.println("✅ " + bedroom.getStatus());
        System.out.println("✅ " + speaker.getStatus());
        System.out.println("✅ " + thermostat.getStatus());
        
        // Test Undo
        System.out.println("\n⏪ === Testing Undo Function === ⏪");
        System.out.println("Undoing last command (temperature decrease):");
        hub.undoCommand();
        
        System.out.println("\nUndoing again (temperature decrease):");
        hub.undoCommand();
        
        // Test Off Commands
        System.out.println("\n🔌 === Testing Off Commands === 🔌");
        System.out.println("Turning off Living Room Light:");
        hub.executeOffCommand(0);
        
        System.out.println("\nTurning off Music:");
        hub.executeOffCommand(2);
        
        // Final Status
        System.out.println("\n🏁 === Final Device Status === 🏁");
        System.out.println("🔹 " + livingRoom.getStatus());
        System.out.println("🔹 " + bedroom.getStatus());
        System.out.println("🔹 " + speaker.getStatus());
        System.out.println("🔹 " + thermostat.getStatus());
        
        // Extensibility Demo
        System.out.println("\n🔧 === Extensibility Demo === 🔧");
        System.out.println("Adding new Smart TV to slot 4...");
        Light smartTV = new Light("Smart TV");
        LightOnCommand tvOn = new LightOnCommand(smartTV);
        LightOffCommand tvOff = new LightOffCommand(smartTV);
        
        hub.setCommand(4, tvOn, tvOff);
        System.out.println("Turning on Smart TV:");
        hub.executeCommand(4);
        System.out.println("📺 " + smartTV.getStatus());
        
        System.out.println("\n🎉 === Demo Complete! === 🎉");
        System.out.println("✨ Command Pattern successfully implemented!");
        System.out.println("✨ All 4 requirements satisfied!");
        System.out.println("✨ New devices can be added without modifying existing code!");
    }
}
