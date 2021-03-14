package me.nerdoron.nerdobot.utils;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Global {
	
	public static void noPermissionEmbed(MessageReceivedEvent commandEvent, String perm ) {
		MessageEmbed noPerm = new EmbedBuilder()
				.setTitle("No permission!")
				.setDescription("You don't have permission to use this feature. This feature requires the `" + perm + "` permisison.")
				.setColor(Color.RED)
				.build();
		commandEvent.getChannel().sendMessage(noPerm).queue();
	}
	
	public static void errorEmbed(MessageReceivedEvent commandEvent) {
		MessageEmbed noPerm = new EmbedBuilder()
				.setTitle("Error")
				.setDescription("There has been an error while executing this feature or command. Don't worry though, I've notified nerdoron.")
				.setColor(Color.RED)
				.build();
		commandEvent.getChannel().sendMessage(noPerm).queue();
	}
	
	public static Color embedColor = new Color(35, 39, 42);
	
}
