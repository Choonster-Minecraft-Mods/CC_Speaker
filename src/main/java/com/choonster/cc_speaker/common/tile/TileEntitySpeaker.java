package com.choonster.cc_speaker.common.tile;

import com.choonster.cc_speaker.Logger;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.Arrays;

public class TileEntitySpeaker extends TileEntity implements IPeripheral, ICommandSender {
	public static final String[] METHOD_NAMES = new String[]{"sendMessage", "sendFormattedMessage", "getLabel", "setLabel"};

	private String label;

	public TileEntitySpeaker() {
		label = "";
	}

	/**
	 * Sends a chat message from the loudspeaker.
	 *
	 * @param message The message to send
	 * @return true if the say command was executed (i.e. result > 0); false otherwise
	 * @throws Exception Throws an exception if it can't get the instance of MinecraftServer.
	 */
	public boolean sendMessage(String message) throws Exception {
		MinecraftServer minecraftserver = MinecraftServer.getServer();

		if (minecraftserver == null) {
			throw new Exception("Couldn't get the instance of MinecraftServer");
		}

		ICommandManager icommandmanager = minecraftserver.getCommandManager();
		return icommandmanager.executeCommand(this, "say " + message) > 0;
	}

	/**
	 * Sends a formatted chat message from the loudspeaker using Java's String.format rules.
	 *
	 * @param format The format string to use
	 * @param args   The format arguments
	 * @return true if the say command was executed (i.e. result > 0); false otherwise
	 * @throws Exception Throws an exception if it can't get the instance of MinecraftServer or if the formatting fails.
	 */
	public boolean sendFormattedMessage(String format, Object[] args) throws Exception {
		/*StringBuilder builder = new StringBuilder();
		for(int i=0;i < args.length; i++){
			Object val = args[i];
			if (i > 0){
				builder.append(" , ");
			}
			builder.append(val);
			builder.append(" : ");
			builder.append(val == null ? "null" : val.getClass());
		}
		Logger.info("sendFormattedMessage: %d, %s", args.length, builder.toString());*/

		return sendMessage(String.format(format, args));
	}

	/**
	 * Get the loudspeaker's label.
	 *
	 * @return The label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Set the loudspeaker's label.
	 *
	 * @param label The new label
	 */
	public void setLabel(String label) {
		this.label = label.trim();
	}

	@Override
	public void readFromNBT(NBTTagCompound tagCompound) {
		super.readFromNBT(tagCompound);

		label = tagCompound.getString("label");
	}

	@Override
	public void writeToNBT(NBTTagCompound tagCompound) {
		super.writeToNBT(tagCompound);

		tagCompound.setString("label", label);
	}

	// IPeripheral methods

	/**
	 * Should return a string that uniquely identifies this type of peripheral.
	 * This can be queried from lua by calling peripheral.getType()
	 *
	 * @return A string identifying the type of peripheral.
	 */
	@Override
	public String getType() {
		return "choonster.cc_speaker";
	}

	/**
	 * Should return an array of strings that identify the methods that this
	 * peripheral exposes to Lua. This will be called once before each attachment,
	 * and should not change when called multiple times.
	 *
	 * @return An array of strings representing method names.
	 * @see #callMethod
	 */
	@Override
	public String[] getMethodNames() {
		return METHOD_NAMES;
	}

	/**
	 * This is called when a lua program on an attached computercraft calls peripheral.call() with
	 * one of the methods exposed by getMethodNames().<br>
	 * <br>
	 * Be aware that this will be called from the ComputerCraft Lua thread, and must be thread-safe
	 * when interacting with minecraft objects.
	 *
	 * @param computer  The interface to the computercraft that is making the call. Remember that multiple
	 *                  computers can be attached to a peripheral at once.
	 * @param context   The context of the currently running lua thread. This can be used to wait for events
	 *                  or otherwise yield.
	 * @param method    An integer identifying which of the methods from getMethodNames() the computercraft
	 *                  wishes to call. The integer indicates the index into the getMethodNames() table
	 *                  that corresponds to the string passed into peripheral.call()
	 * @param arguments An array of objects, representing the arguments passed into peripheral.call().<br>
	 *                  Lua values of type "string" will be represented by Object type String.<br>
	 *                  Lua values of type "number" will be represented by Object type Double.<br>
	 *                  Lua values of type "boolean" will be represented by Object type Boolean.<br>
	 *                  Lua values of any other type will be represented by a null object.<br>
	 *                  This array will be empty if no arguments are passed.
	 * @return An array of objects, representing values you wish to return to the lua program.<br>
	 * Integers, Doubles, Floats, Strings, Booleans and null be converted to their corresponding lua type.<br>
	 * All other types will be converted to nil.<br>
	 * You may return null to indicate no values should be returned.
	 * @throws Exception If you throw any exception from this function, a lua error will be raised with the
	 *                   same message as your exception. Use this to throw appropriate errors if the wrong
	 *                   arguments are supplied to your method.
	 * @see #getMethodNames
	 */
	@Override
	public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception {
		//Logger.info("callMethod %d %d", method, arguments.length);

		Object[] result = null;

		switch (method) {
			case 0:
				if (arguments.length < 1 || !(arguments[0] instanceof String)) {
					throw new Exception("Usage: success : boolean = sendMessage(message : string)");
				}
				result = new Boolean[]{sendMessage((String) arguments[0])};
				break;

			case 1:
				if (arguments.length < 1 || !(arguments[0] instanceof String)) {
					throw new Exception("Usage: success : boolean = sendFormattedMessage(format : string, ... : any)");
				}
				result = new Boolean[]{sendFormattedMessage((String) arguments[0], Arrays.copyOfRange(arguments, 1, arguments.length))};
				break;

			case 2:
				result = new String[]{getLabel()};
				break;

			case 3:
				if (arguments.length < 1 || !(arguments[0] instanceof String)) {
					throw new Exception("Usage: setLabel(label : string)");
				}
				setLabel((String) arguments[0]);
				break;
		}

		return result;
	}

	/**
	 * Is called when canAttachToSide has returned true, and a computercraft is attaching to the peripheral.
	 * This will occur when a peripheral is placed next to an active computercraft, when a computercraft is turned on next to a peripheral,
	 * or when a turtle travels into a square next to a peripheral.
	 * Between calls to attach() and detach(), the attached computercraft can make method calls on the peripheral using peripheral.call().
	 * This method can be used to keep track of which computers are attached to the peripheral, or to take action when attachment
	 * occurs.<br>
	 * <br>
	 * Be aware that this will be called from the ComputerCraft Lua thread, and must be thread-safe
	 * when interacting with minecraft objects.
	 *
	 * @param computer The interface to the computercraft that is being attached. Remember that multiple
	 *                 computers can be attached to a peripheral at once.
	 * @see #detach
	 */
	@Override
	public void attach(IComputerAccess computer) {
		// No-op
	}

	/**
	 * Is called when a computercraft is detaching from the peripheral.
	 * This will occur when a computercraft shuts down, when the peripheral is removed while attached to computers,
	 * or when a turtle moves away from a square attached to a peripheral.
	 * This method can be used to keep track of which computers are attached to the peripheral, or to take action when detachment
	 * occurs.<br>
	 * <br>
	 * Be aware that this will be called from the ComputerCraft Lua thread, and must be thread-safe
	 * when interacting with minecraft objects.
	 *
	 * @param computer The interface to the computercraft that is being detached. Remember that multiple
	 *                 computers can be attached to a peripheral at once.
	 * @see #detach
	 */
	@Override
	public void detach(IComputerAccess computer) {
		// No-op
	}

	/**
	 * TODO: Document me
	 *
	 * @param other
	 */
	@Override
	public boolean equals(IPeripheral other) {
		return other instanceof TileEntitySpeaker && other == this;
	}

	// ICommandSender methods

	/**
	 * Gets the name of this command sender (usually username, but possibly "Rcon")
	 */
	@Override
	public String getCommandSenderName() {
		return !label.equals("") ? label : StatCollector.translateToLocal("tile.cc_speaker.name");
	}

	@Override
	public void sendChatToPlayer(ChatMessageComponent chatmessagecomponent) {
		// No-op
	}

	/**
	 * Returns true if the command sender is allowed to use the given command.
	 *
	 * @param permissionLevel
	 * @param commandName
	 */
	@Override
	public boolean canCommandSenderUseCommand(int permissionLevel, String commandName) {
		return commandName.equals("say");
	}

	/**
	 * Return the position for this command sender.
	 */
	@Override
	public ChunkCoordinates getPlayerCoordinates() {
		return new ChunkCoordinates(xCoord, yCoord, zCoord);
	}

	@Override
	public World getEntityWorld() {
		return worldObj;
	}
}
