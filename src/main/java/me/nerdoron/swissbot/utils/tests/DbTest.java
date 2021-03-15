package me.nerdoron.swissbot.utils.tests;

import org.slf4j.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.swissbot.commandmanager.Command;
import me.nerdoron.swissbot.utils.Database;
import me.nerdoron.swissbot.utils.Global;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DbTest extends Command {

	@Override
	public void execute(MessageReceivedEvent commandEvent, Object[] args, Logger logger, Dotenv dotenv) {
		
		if(!(commandEvent.getAuthor().getId().equals("221204198287605770"))) {
			Global.noPermissionEmbed(commandEvent, "BOT_OWNER");
			return;
		}
		
		try {
			Database.connect();
			commandEvent.getChannel().sendMessage("true").queue();
		} catch (Exception ex) {
			logger.error(ex.toString());
			commandEvent.getChannel().sendMessage("false");
		}
	}

}
