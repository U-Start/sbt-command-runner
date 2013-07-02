package com.eligotech.sbt.plugins

import sbt.Project.{ Setting => ProjectSetting, _ }
import sbt._

trait CommandsRunner extends Logging with Validations {
  protected type CommandName = String
  protected type Configuration = Map[CommandName, RunnerCommand]
  protected type Error = String

  protected case class RunnerCommand(binary: String, parameters: Seq[String])

  protected object Runner {
    val commands = SettingKey[Configuration]("launcher-commands", "compact code tasks and their parameters")

    def settings: ProjectSetting[InputTask[Unit]] = runCommand <<= makeInputTask
  }

  private def makeInputTask: Initialize[InputTask[Unit]] = inputTask { (argTask: TaskKey[Seq[String]]) =>
    (argTask, Runner.commands) map { (args: Seq[String], config: Configuration) =>
      trace(runCommand(args, config))
    }
  }

  private def runCommand = InputKey[Unit]("run-command", "Run to compact code")

  private def trace: (Validation[Unit]) => Unit = {
    case Right(int) => logInfo("done")
    case Left(msg)  => logError(msg)
  }

  private def runCommand(args: Seq[String], config: Configuration): Validation[Unit] =
    for {
      n <- getCommandName(args)
      c <- getCommand(n, config)
      r <- run(c)
    } yield r

  private def run(command: RunnerCommand): Validation[Unit] =
    safe(Process(command.binary, command.parameters).run().exitValue()) map { _ => () }

  private def getCommand(name: String, commands: Configuration): Validation[RunnerCommand] = commands.get(name) match {
    case Some(command) => valid(command)
    case None          => failure("Missing command '%s' definition" format name)
  }

  private def getCommandName(args: Seq[String]): Validation[String] = args.headOption match {
    case Some(cmd) => valid(cmd)
    case _         => failure("missing command parameter")
  }
}
