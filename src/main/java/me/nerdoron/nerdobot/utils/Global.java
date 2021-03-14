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
	
}
