job("Build and publish") {
    container(displayName = "Build and notify", image = "gradle:7.2-jre11") {
        kotlinScript { api ->
            val channel = ChannelIdentifier.Channel(ChatChannel.FromName("developers"))
            try {
                api.gradlew("build")
                val content = ChatMessage.Text("Build succeeded")
                api.space().chats.messages.sendMessage(channel = channel, content = content)
            } catch (ex: Exception) {
                val content = ChatMessage.Text("Build failed")
                api.space().chats.messages.sendMessage(channel = channel, content = content)
                val errMsg = ChatMessage.Text("${ex.message}")
                api.space().chats.messages.sendMessage(channel = channel, content = content)
            }
        }
    }
}