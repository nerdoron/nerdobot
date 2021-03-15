package me.nerdoron.nerdobot.commands.swissspecific.askstaff;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.slf4j.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.nerdobot.commandmanager.Command;
import me.nerdoron.nerdobot.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AskStaffCommand extends Command {

	public static EmbedBuilder askYesId;

	@Override
	public void execute(MessageReceivedEvent commandEvent, Object[] args, Logger logger, Dotenv dotenv) {

		if (args.length == 0) {
			commandEvent.getChannel().sendMessage("You must provide a question!").queue();
			return;
		}
		
		TextChannel askstaff = commandEvent.getGuild().getTextChannelById("820790675503054898");

		MessageEmbed askNoId = new EmbedBuilder()
				.setAuthor(commandEvent.getAuthor().getAsTag() + "'s question", null, commandEvent.getAuthor().getAvatarUrl())
				.setDescription("Getting question...")		
				.addField("Author ID", commandEvent.getAuthor().getId(), true)
				.addField("Question ID", "Getting id...", true).setColor(Color.green).build();

		askstaff.sendMessage(askNoId).queue((message) -> {
			String messageId = message.getId();
			String question = " ";
			for (int i = 0; i < args.length; i++) {
				question = question + (i == 0 ? "" : " ") + args[i].toString();
			}
			
			commandEvent.getChannel().sendMessage(":white_check_mark: Your question was sent to the staff team!\n**Please remember that sending troll questions will result in a warning. If you would like to retract your question, use +retract <id>**").queue();

			askYesId = new EmbedBuilder()
					.setAuthor(commandEvent.getAuthor().getAsTag() + "'s question", null, commandEvent.getAuthor().getAvatarUrl())
					.setDescription(question).addField("Author ID", commandEvent.getAuthor().getId(), true)
					.addField("Question ID", messageId, true).setColor(Color.green);
			MessageEmbed ask = askYesId.build();
			Connection con = Database.connect();
			PreparedStatement ps = null;
			String SQL = "insert into askstaff (questionId, questionAuthor, answer) values(?,?,?)";

			try {
				ps = con.prepareStatement(SQL);
				ps.setString(1, messageId);
				ps.setString(2, commandEvent.getAuthor().getId());
				ps.setInt(3, 0);
				ps.execute();	
			} catch (Exception ex) {
				logger.error(ex.toString());
			}
			message.editMessage(ask).queue();
		});

	}
}