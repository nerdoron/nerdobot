package me.nerdoron.nerdobot.commands.global.afksystem;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.slf4j.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.nerdobot.commandmanager.Command;
import me.nerdoron.nerdobot.utils.Database;
import me.nerdoron.nerdobot.utils.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AFKCommand extends Command {
	
	@Override
	public void execute(MessageReceivedEvent commandEvent, Object[] args, Logger logger, Dotenv dotenv) {
		String reason = " ";

		if (args.length == 0) {
			reason = "No reason specified.";
			addAfkPerson(commandEvent.getAuthor().getId(), reason, logger, commandEvent);
			return;
		} else {
			for (int i = 0; i < args.length; i++) {
				reason = reason + (i == 0 ? "" : " ") + args[i].toString();
			}
			addAfkPerson(commandEvent.getAuthor().getId(), reason, logger, commandEvent);
			return;
		}
	}

	private void addAfkPerson(String UID, String reason, Logger logger, MessageReceivedEvent commandEvent) {
		Connection con = Database.connect();
		PreparedStatement ps = null;
		String SQL = "insert into afk (UID, REASON) values(?,?)";

		try {
			ps = con.prepareStatement(SQL);
			ps.setString(1, UID);
			ps.setString(2, reason);
			ps.execute();
			MessageEmbed afk = new EmbedBuilder()
					.setTitle(":wave: Goodbye, " + commandEvent.getAuthor().getAsTag())
					.setDescription("I've sucessfully set your AFK status!")
					.addField("Reason", reason, false)
					.setColor(Global.embedColor)
					.build();
			commandEvent.getChannel().sendMessage(afk).queue();
		} catch (Exception ex) {
			logger.error(ex.toString());
			Global.errorEmbed(commandEvent);
		}

	}

}
