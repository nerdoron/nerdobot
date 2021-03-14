package me.nerdoron.nerdobot.commandmanager;


import java.sql.Timestamp;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public abstract class Command {
	
	public void executeGlobal(MessageReceivedEvent commandEvent, String cmd, Object[] args) {
		final Logger logger = LoggerFactory.getLogger(Command.class);
		Dotenv dotenv = Dotenv.configure().directory("D:\\dev\\eclipse15\\loungepd\\.env").load();
    	try {
        execute(commandEvent, args, logger, dotenv);
    	} catch (Exception ex ) {
    		commandEvent.getChannel().sendMessage("There has been an error executing the command, don't worry though, the developer has been notified.");
    		logger.error("Error while tryng to execute command " + commandEvent.getMessage().getContentRaw() + "\n\n", ex);
    		String error = ExceptionUtils.getStackTrace(ex);
    		TextChannel channel = commandEvent.getJDA().getTextChannelById(dotenv.get("DEBUG_ID"));
    		channel.sendMessage("Bruh, <@" + dotenv.get("DEV_ID") + ">, Error detected!").queue();
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            MessageEmbed errorEmbed = new EmbedBuilder()
                    .setTitle("Error!")
                    .addField("Cause", String.valueOf(error), false)
                    .addField("Command Used", commandEvent.getMessage().getContentRaw(), false)
                    .addField("Guild", commandEvent.getGuild().getName() + "\n" + commandEvent.getGuild().getId(), false)
                    .addField("Channel", commandEvent.getChannel().getName() + "\n" + commandEvent.getChannel().getId(), false)
                    .addField("Time", String.valueOf(timestamp), false)
                    .build();
            channel.sendMessage(errorEmbed).queue();
    	}
	}
	
    public abstract void execute(MessageReceivedEvent commandEvent, Object[] args, Logger logger, Dotenv dotenv);

	
	
}