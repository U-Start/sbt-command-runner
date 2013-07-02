sbt-command-runner
==================

Wraps sbt Process invocation in a convenient set of predefined commands.

In order to install the plugin add the following line to your plugins.sbt file:

    addSbtPlugin("com.eligotech" %% "sbt-command-runner" % "0.1-SNAPSHOT")

The plugin comes as a trait your SBT application objects must derive.
Make sure you aways import the trait definition in your scope

    import com.eligotech.sbt.plugins.CommandsRunner

And make your SBT application extend the trait as in :

    import com.eligotech.sbt.plugins.CommandsRunner
    ...
    object ApplicationBuild extends Build  with CommandsRunner
    ...

We provide two settings to initialize:

* The commands settings as map of commands indexed by a logical name
* The initialization of the command task itself


Example
------------

Suppose one wants to message a text to the local user.

The project settings initialization looks like:

       .settings(Runner.commands := Map("say-hello" -> RunnerCommand("msg", Seq("omadas", """"hello!""""))))
       .settings(Runner.settings: _*)


where

* say-hello is the logical name of the command to execute
* msg is the name of the program to launch
* Seq("omadas", """"hello!"""") is the sequence of commands to launch


In order to execute your logical command from sbt console use the run-command task as in

    $ run-command say-hello




