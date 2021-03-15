package me.nerdoron.nerdobot.commands.swissspecific.askstaff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.slf4j.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.nerdobot.commandmanager.Command;
import me.nerdoron.nerdobot.utils.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RetractCommand extends Command {

	@Override
	public void execute(MessageReceivedEvent commandEvent, Object[] args, Logger logger, Dotenv dotenv) {
		String authorId = commandEvent.getAuthor().getId();
		String authorQuestionSlection = args[0].toString();
		try {
			String SQL1 = "select * from askstaff where questionId=?";

			Connection con = Database.connect();
			PreparedStatement ps1 = con.prepareStatement(SQL1);
			ps1.setString(1, authorQuestionSlection);
			ResultSet rs = ps1.executeQuery();
			authorQuestionSlection = rs.getString("questionId");
			String questionAuthorId = rs.getString("questionAuthor");
			int answeredAlready = rs.getInt("answer");
			ps1.close();

			if(answeredAlready == 1) {
				commandEvent.getChannel().sendMessage(":x: Question already answered!").queue();
				return;
			}
			
			if(!(questionAuthorId.equals(authorId))) {
				commandEvent.getChannel().sendMessage(":x: Only the author of the original question can do this!").queue();
				return; 
			}
			
			
			TextChannel askstaff = commandEvent.getGuild().getTextChannelById("820790675503054898");
			MessageEmbed eb = new EmbedBuilder(AskStaffCommand.askYesId).setDescription("[question retracted by author]").build();
			askstaff.editMessageById(authorQuestionSlection, eb).queue();
			
			String SQL2 = "delete from askstaff where questionId=?";
			PreparedStatement ps2 = con.prepareStatement(SQL2);
			ps2.setString(1, authorQuestionSlection);
			ps2.execute();
			
			commandEvent.getChannel().sendMessage(":white_check_mark: Success!").queue();
			
			
			
			
		} catch (Exception ex) {
			
		}
		
	}
	
	

}
