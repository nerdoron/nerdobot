package me.nerdoron.swissbot.commands.global;

import org.slf4j.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.swissbot.commandmanager.Command;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand extends Command {

	@Override
	public void execute(MessageReceivedEvent commandEvent, Object[] args, Logger logger, Dotenv dotenv) {
        long ping = commandEvent.getJDA().getGatewayPing();
        commandEvent.getChannel().sendMessage("Pong! " + ping + "ms.").queue();		
	}


}
