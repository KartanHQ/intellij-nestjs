package com.nekofar.milad.intellij.truffle.pages

import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.data.RemoteComponent
import com.intellij.remoterobot.fixtures.CommonContainerFixture
import com.intellij.remoterobot.fixtures.DefaultXpath
import com.intellij.remoterobot.fixtures.FixtureName
import com.intellij.remoterobot.search.locators.byXpath
import java.time.Duration

fun RemoteRobot.welcomeFrame(function: WelcomeFrame.() -> Unit) {
    find(WelcomeFrame::class.java, Duration.ofSeconds(10)).apply(function)
}

@FixtureName("Welcome Frame")
@DefaultXpath("type", "//div[@class='FlatWelcomeFrame']")
class WelcomeFrame(remoteRobot: RemoteRobot, remoteComponent: RemoteComponent) :
    CommonContainerFixture(remoteRobot, remoteComponent) {
    val createNewProjectLink
        get() = actionLink(
            byXpath(
                "New Project",
                "//div[(@class='MainButton' and @text='New Project') or (@accessiblename='New Project' and @class='JButton')]"
            )
        )
}