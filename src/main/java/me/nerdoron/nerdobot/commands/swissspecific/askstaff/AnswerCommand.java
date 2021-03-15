package me.nerdoron.nerdobot.commands.swissspecific.askstaff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.nerdobot.commandmanager.Command;
import me.nerdoron.nerdobot.utils.Database;
import me.nerdoron.nerdobot.utils.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class AnswerCommand extends Command {

	@Override
	public void execute(MessageReceivedEvent commandEvent, Object[] args, Logger logger, Dotenv dotenv) {
		if (!(commandEvent.getMember().hasPermission(Permission.MESSAGE_MANAGE))) {
			Global.noPermissionEmbed(commandEvent, "MANAGE_MESSAGES");
			return;
		}

		String messageId = args[0].toString();
		String answer = " ";
		for (int i = 1; i < args.length; i++) {
			answer = answer + (i == 1 ? "" : " ") + args[i].toString();
		}

		String SQL1 = "select * from askstaff where questionId=?";

		TextChannel askstaff = commandEvent.getGuild().getTextChannelById("820790675503054898");

		try {
			Connection con = Database.connect();
			PreparedStatement ps1 = con.prepareStatement(SQL1);
			ps1.setString(1, messageId);
			ResultSet rs = ps1.executeQuery();
			messageId = rs.getString("questionId");
			String authorId = rs.getString("questionAuthor");
			int answeredAlready = rs.getInt("answer");
			ps1.close();

			if(answeredAlready == 1) {
				commandEvent.getChannel().sendMessage(":x: Question already answered!").queue();
				return;
			}
			String SQL2 = "replace into askstaff(questionId, questionAuthor, answer) values(?,?,?)";
			PreparedStatement ps2 = con.prepareStatement(SQL2);
			ps2.setString(1, messageId);
			ps2.setString(2, authorId);
			ps2.setInt(3, 1);
			ps2.execute();
			
			MessageEmbed eb = new EmbedBuilder(AskStaffCommand.askYesId)
					.addField("Answer by " + commandEvent.getAuthor().getAsTag() + ":", answer, false).build();

			askstaff.editMessageById(messageId, eb).queue();

			commandEvent.getChannel().sendMessage(":white_check_mark: Sucess!").queue();

			
		} catch (Exception ex) {
			Global.errorEmbed(commandEvent);
			logger.error(ex.toString());
		}

	}

}
