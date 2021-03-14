package me.nerdoron.nerdobot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cdimascio.dotenv.Dotenv;
import me.nerdoron.nerdobot.commandmanager.CommandManager;
import me.nerdoron.nerdobot.commands.global.PingCommand;
import me.nerdoron.nerdobot.commands.global.fun.EightBall;
import me.nerdoron.nerdobot.utils.tests.DbTest;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {
	
	private static final Logger logger = LoggerFactory.getLogger(Main.class);
	
	private static String prefix = "+";
	
	
	public static void main(String[] args) throws IllegalArgumentException {
		try {
			logger.info("Trying to setup the bot...");
			logger.info("Trying to setup load env file...");
			dotenv();
		} catch (Exception ex) {
			logger.error("An exception occurred while trying to setup the bot!", ex);
		}
	}
	
	
	private static void dotenv() {
		try {
			Dotenv dotenv = Dotenv.load();
			logger.info("Loaded env file!");
			logger.info("Trying to load the bot...");
			bot(dotenv);
		} catch (Exception ex) {
			logger.error("An exception occurred while trying to load the dotenv file!", ex);
		}
	}
	

	private static void bot(Dotenv dotenv) {
		String token = dotenv.get("TOKEN");
		try {
			JDA api = JDABuilder.createDefault(token).enableIntents(
					GatewayIntent.GUILD_MESSAGE_REACTIONS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS
					)
					.build();
			api.getPresence().setActivity(Activity.watching("Pogchamp moment"));
			registerCommand(api);
		} catch (Exception ex) {
			logger.error("An exception occurred while trying to load the bot!", ex);
		}
	}
	
	private static void registerCommand(JDA jda) {
		try {
			new CommandManager(jda, prefix)
			.registerCommand(new DbTest(), "dbtest")
			.registerCommand(new PingCommand(), "ping", "pong")
			.registerCommand(new EightBall(), "8ball", "eightball");


		} catch (Exception ex) {
			
		}
	}
	
	

}
