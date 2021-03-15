package me.nerdoron.swissbot.commands.global.fun;

import org.slf4j.Logger;

import io.github.cdimascio.dotenv.Dotenv;
import me.duncte123.botcommons.web.WebUtils;
import me.nerdoron.swissbot.commandmanager.Command;
import me.nerdoron.swissbot.utils.Global;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class JokeCommand extends Command {

	@Override
	public void execute(MessageReceivedEvent commandEvent, Object[] args, Logger logger, Dotenv dotenv) {
		
		WebUtils.ins.getJSONObject("https://official-joke-api.appspot.com/random_joke").async((json) -> {	
			final String title = json.get("setup").asText();
			final String body = json.get("punchline").asText();
			
			MessageEmbed joke = new EmbedBuilder()
					.setTitle(title)
					.setDescription("||" + body + "||")
					.setColor(Global.embedColor)
					.build();
			commandEvent.getChannel().sendMessage(joke).queue();
		});
		
	}
	

}
