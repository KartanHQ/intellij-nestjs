package com.nekofar.milad.intellij.truffle

import com.automation.remarks.junit5.Video
import com.intellij.remoterobot.RemoteRobot
import com.intellij.remoterobot.search.locators.byXpath
import com.intellij.remoterobot.stepsProcessing.step
import com.intellij.remoterobot.utils.waitFor
import com.nekofar.milad.intellij.truffle.fixtures.terminal
import com.nekofar.milad.intellij.truffle.pages.dialog
import com.nekofar.milad.intellij.truffle.pages.idea
import com.nekofar.milad.intellij.truffle.pages.welcomeFrame
import com.nekofar.milad.intellij.truffle.utils.RemoteRobotExtension
import com.nekofar.milad.intellij.truffle.utils.StepsLogger
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import java.time.Duration.ofSeconds

@TestMethodOrder(OrderAnnotation::class)
@ExtendWith(RemoteRobotExtension::class)
class UITest {
    init {
        StepsLogger.init()
    }

    @AfterEach
    fun closeProject(remoteRobot: RemoteRobot) = with(remoteRobot) {
        idea {
            menuBar.select("File", "Close Project")
        }
    }

    @Test
    @Video
    @Order(1)
    fun createNewProject(remoteRobot: RemoteRobot) = with(remoteRobot) {
        welcomeFrame {
            createNewProjectLink.click()
            dialog("New Project") {
                findText("JavaScript").click()
                jList(
                    byXpath(
                        "//div[contains(@visible_text_keys, 'create.react.app.name')]"
                    )
                ).clickItem("Nest")
                button("Next").click()
                button("Finish").click()
            }
        }
        idea {
            waitForFinishBackgroundTasks()
            step("Find terminal") {
                terminal().click()
            }
            step("Find config file") {
                with(projectViewTree) {
                    if (hasText("nest-cli.json").not()) {
                        findText(projectName).doubleClick()
                        waitFor(ofSeconds(10)) { hasText("nest-cli.json") }
                    }
                    findText("nest-cli.json").click()
                }
            }
        }
    }
}