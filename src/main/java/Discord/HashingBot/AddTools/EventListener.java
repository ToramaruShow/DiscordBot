package Discord.HashingBot.AddTools;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateOnlineStatusEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventListener extends ListenerAdapter {
    private Dotenv config;

    public Dotenv getConfig() { return config; }

    @Override
    public void onMessageReactionAdd(@NotNull MessageReactionAddEvent event) {
        User user = event.getUser();
        String jumpLink = event.getJumpUrl();
        String emoji = event.getReaction().getReactionEmote().getAsReactionCode();
        String channel = event.getChannel().getAsMention();

        String message = user.getAsTag() + " reacted to a [message](" + jumpLink + ") with " + emoji + " in the " + channel + " channel!";
        event.getChannel().sendMessage(message).queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        // After August 2022 will require gateway intent
        String message = event.getMessage().getContentRaw();
        if (message.contains("ping")) {
            event.getChannel().sendMessage("pong").queue();
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {
        String avatar = event.getUser().getEffectiveAvatarUrl();
        System.out.println(avatar);
    }

    @Override
    public void onUserUpdateOnlineStatus(@NotNull UserUpdateOnlineStatusEvent event) {
        int onlineMembers = 0;
        for (Member member : event.getGuild().getMembers()) {
            if (member.getOnlineStatus() == OnlineStatus.ONLINE) {
                onlineMembers++;
            }
        }
        if (event.getMember().getOnlineStatus() == OnlineStatus.ONLINE) {
            String message = event.getUser().getAsTag() + " updated their online status! There are " + onlineMembers + " members online now!";

            // event.getGuild().getDefaultChannel().sendMessage(message).queue();
            // このギルド　　　　　のデフォチャンネル　　　　にメッセージ　　　　　　？
        }
    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent event) {
        config = Dotenv.configure().load();
        long vc1 = Long.parseLong(config.get("VC-1")),vc2 = Long.parseLong(config.get("VC-2")),vc3 = Long.parseLong(config.get("VC-3"))
                ,vc4 = Long.parseLong(config.get("VC-4")),vc5 = Long.parseLong(config.get("VC-5"));
        String selvc = null,selvcm = null;
        if (vc1 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC1");
        } else if (vc2 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC2");
        } else if (vc2 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC3");
        } else if (vc2 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC4");
        } else if (vc2 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC5");
        }
        if (vc1 == event.getChannelLeft().getIdLong()) {
            selvcm = config.get("TVC1");
        } else if (vc2 == event.getChannelLeft().getIdLong()) {
            selvcm = config.get("TVC2");
        } else if (vc2 == event.getChannelLeft().getIdLong()) {
            selvcm = config.get("TVC3");
        } else if (vc2 == event.getChannelLeft().getIdLong()) {
            selvcm = config.get("TVC4");
        } else if (vc2 == event.getChannelLeft().getIdLong()) {
            selvcm = config.get("TVC5");
        }
        if (selvc != null) {
            String user = event.getMember().getNickname();
            String vname = event.getChannelLeft().getName();
            String vnamem = event.getChannelJoined().getName();
            String message = user + " Join [" + vname + "]";
            String messagem = user + " Move From [" + vname + "] to ["+vnamem+"]";
            TextChannel textChannel = event.getGuild().getTextChannelsByName(selvc, false).get(0);//大・小文字無視＝true
            TextChannel textChannelmove = event.getGuild().getTextChannelsByName(selvcm, false).get(0);//大・小文字無視＝true
            textChannel.sendMessage(message).queue();
            textChannelmove.sendMessage(messagem).queue();
        }
    }

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        config = Dotenv.configure().load();
        long vc1 = Long.parseLong(config.get("VC-1")),vc2 = Long.parseLong(config.get("VC-2")),vc3 = Long.parseLong(config.get("VC-3"))
                ,vc4 = Long.parseLong(config.get("VC-4")),vc5 = Long.parseLong(config.get("VC-5"));
        String selvc = null;
        if (vc1 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC1");
        } else if (vc2 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC2");
        } else if (vc2 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC3");
        } else if (vc2 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC4");
        } else if (vc2 == event.getChannelJoined().getIdLong()) {
            selvc = config.get("TVC5");
        }
        if (selvc != null) {
            String user = event.getMember().getNickname();
            String vname = event.getChannelJoined().getName();
            String message = user + " Join [" + vname + "]";
            TextChannel textChannel = event.getGuild().getTextChannelsByName(selvc, false).get(0);//大・小文字無視＝true
            textChannel.sendMessage(message).queue();
        }
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent event) {
        config = Dotenv.configure().load();
        long vc1 = Long.parseLong(config.get("VC-1")),vc2 = Long.parseLong(config.get("VC-2")),vc3 = Long.parseLong(config.get("VC-3"))
                ,vc4 = Long.parseLong(config.get("VC-4")),vc5 = Long.parseLong(config.get("VC-5"));
        String selvc = null;
        if (vc1 == event.getChannelLeft().getIdLong()) {
            selvc = config.get("TVC1");
        } else if (vc2 == event.getChannelLeft().getIdLong()) {
            selvc = config.get("TVC2");
        } else if (vc2 == event.getChannelLeft().getIdLong()) {
            selvc = config.get("TVC3");
        } else if (vc2 == event.getChannelLeft().getIdLong()) {
            selvc = config.get("TVC4");
        } else if (vc2 == event.getChannelLeft().getIdLong()) {
            selvc = config.get("TVC5");
        }
        if (selvc != null) {
            String user = event.getMember().getNickname();
            String vname = event.getChannelLeft().getName();
            String message = user + " Leave [" + vname + "]";
            TextChannel textChannel = event.getGuild().getTextChannelsByName(selvc, false).get(0);//大・小文字無視＝true
            textChannel.sendMessage(message).queue();
        }
    }
}