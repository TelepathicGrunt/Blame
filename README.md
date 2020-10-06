<p style="text-align: center;"><span style="font-size: 14px;"><strong>Got a modpack where a feature or structure is crashing and the crashlogs are telling you nothing?</strong></span></p>
<p style="text-align: center;"><span style="font-size: 14px;"><strong>When you add two worldgen mods together, do features or structures stop spawning?</strong></span></p>
<p style="text-align: center;"><span style="font-size: 18px;"><strong>FEAR NO MORE!!!</strong></span></p>
<p>&nbsp;</p>
<p><strong>Blame</strong> will help you place the <em>blame</em> on the correct mod easier! It prints out into the crashlog what the name of the feature is, the structure name, and the biome that it was in when it crashed. Furthermore, it also shoves the info into the latest.log file too so you don't have to look at two log file separately anymore! The latest.log file will hold all the info you'll need. <strong>Blame</strong> is currently for 1.16.2 and 1.16.3 and no, I will not backport! lmao. Though anyone can PR a backport and I'll approve of it on the GitHub. :)</p>
<p>&nbsp;</p>
<p>Also, when loading up a world and a broken JSON file is found, this mod will print that file's resourcelocation to the latest.log file so you know which mod has the broken JSON file!&nbsp;&nbsp;</p>
<p>&nbsp;</p>
<p style="text-align: left;"><br /><span style="font-size: 24px;"><strong>NOT CONVINCED THIS MOD IS WORTH IT?!</strong></span></p>
<p style="text-align: left;"><span style="font-size: 14px;"><strong>Check out this crashlog. Can you figure out what feature could even be the problem? I certainly can't.&nbsp;</strong></span></p>
<div class="spoiler">
<pre>---- Minecraft Crash Report ----<br /><span class="hljs-regexp">//</span> Uh... Did I <span class="hljs-keyword">do</span> that?
<br />
<br />Time: <span class="hljs-number">9</span>/<span class="hljs-number">3</span>/<span class="hljs-number">20</span> <span class="hljs-number">11</span>:<span class="hljs-number">50</span> AM
<br />Description: Feature placement
<br />
<br />java.lang.IllegalArgumentException: bound must be positive
<br />   at java.util.Random.nextInt(Random.java:<span class="hljs-number">388</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at net.minecraft.world.gen.placement.RangePlacement.getPositions(RangePlacement.java:<span class="hljs-number">16</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.placement.RangePlacement.getPositions(RangePlacement.java:<span class="hljs-number">8</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.placement.SimplePlacement.func_241857_a(SimplePlacement.java:<span class="hljs-number">15</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.placement.ConfiguredPlacement.func_242876_a(ConfiguredPlacement.java:<span class="hljs-number">24</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>) ~[?:?] {re:mixin,re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.lambda$func_241855_a$0(DecoratedFeature.java:<span class="hljs-number">19</span>) ~[?:?] {re:classloading}
<br />   at java.util.stream.Streams$StreamBuilderImpl.forEachRemaining(Streams.java:<span class="hljs-number">419</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:<span class="hljs-number">580</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>) ~[?:?] {re:mixin,re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.lambda$func_241855_a$0(DecoratedFeature.java:<span class="hljs-number">19</span>) ~[?:?] {re:classloading}
<br />   at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:<span class="hljs-number">184</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.IntPipeline$4$1.accept(IntPipeline.java:<span class="hljs-number">250</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.Streams$RangeIntSpliterator.forEachRemaining(Streams.java:<span class="hljs-number">110</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.Spliterator$OfInt.forEachRemaining(Spliterator.java:<span class="hljs-number">693</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:<span class="hljs-number">482</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:<span class="hljs-number">472</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:<span class="hljs-number">151</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:<span class="hljs-number">174</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:<span class="hljs-number">234</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:<span class="hljs-number">418</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>) ~[?:?] {re:mixin,re:classloading}
<br />   at net.minecraft.world.biome.Biome.func_242427_a(Biome.java:<span class="hljs-number">255</span>) ~[?:?] {re:mixin,re:classloading,pl:mixin:APP:feature_blame.mixins.json:BiomeMixin,pl:mixin:A}
<br />   at net.minecraft.world.gen.ChunkGenerator.func_230351_a<span class="hljs-number">_</span>(ChunkGenerator.java:<span class="hljs-number">192</span>) ~[?:?] {re:mixin,re:classloading}
<br />   at net.minecraft.world.chunk.ChunkStatus.lambda$static$9(ChunkStatus.java:<span class="hljs-number">77</span>) ~[?:?] {re:classloading,pl:accesstransformer:B}
<br />   at net.minecraft.world.chunk.ChunkStatus.doGenerationWork(ChunkStatus.java:<span class="hljs-number">198</span>) ~[?:?] {re:classloading,pl:accesstransformer:B}
<br />   at net.minecraft.world.server.ChunkManager.lambda$null$18(ChunkManager.java:<span class="hljs-number">541</span>) ~[?:?] {re:classloading}
<br />   at com.mojang.datafixers.util.Either$Left.map(Either.java:<span class="hljs-number">38</span>) ~[datafixerupper-<span class="hljs-number">4.0</span>.<span class="hljs-number">26</span>.jar:?] {re:classloading,re:classloading}
<br />   at net.minecraft.world.server.ChunkManager.lambda$chunkGenerate$2<span class="hljs-number">0</span>(ChunkManager.java:<span class="hljs-number">539</span>) ~[?:?] {re:classloading}
<br />   at java.util.concurrent.CompletableFuture.uniCompose(CompletableFuture.java:<span class="hljs-number">966</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.CompletableFuture$UniCompose.tryFire(CompletableFuture.java:<span class="hljs-number">940</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.CompletableFuture$Completion.run(CompletableFuture.java:<span class="hljs-number">456</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at net.minecraft.world.chunk.ChunkTaskPriorityQueueSorter.lambda$null$1(ChunkTaskPriorityQueueSorter.java:<span class="hljs-number">44</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.driveOne(DelegatedTaskExecutor.java:<span class="hljs-number">88</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.driveWhile(DelegatedTaskExecutor.java:<span class="hljs-number">132</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.run(DelegatedTaskExecutor.java:<span class="hljs-number">100</span>) ~[?:?] {re:classloading}
<br />   at java.util.concurrent.ForkJoinTask$RunnableExecuteAction.exec(ForkJoinTask.java:<span class="hljs-number">1402</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:<span class="hljs-number">289</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:<span class="hljs-number">1056</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:<span class="hljs-number">1692</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:<span class="hljs-number">157</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />
<br />
<br />A detailed walkthrough of the error, its code path <span class="hljs-keyword">and</span> all known details is as follows:
<br />---------------------------------------------------------------------------------------
<br />
<br />-- Head --
<br />Thread: Server thread
<br />Stacktrace:
<br />   at java.util.Random.nextInt(Random.java:<span class="hljs-number">388</span>)
<br />   at net.minecraft.world.gen.placement.RangePlacement.getPositions(RangePlacement.java:<span class="hljs-number">16</span>)
<br />   at net.minecraft.world.gen.placement.RangePlacement.getPositions(RangePlacement.java:<span class="hljs-number">8</span>)
<br />   at net.minecraft.world.gen.placement.SimplePlacement.func_241857_a(SimplePlacement.java:<span class="hljs-number">15</span>)
<br />   at net.minecraft.world.gen.placement.ConfiguredPlacement.func_242876_a(ConfiguredPlacement.java:<span class="hljs-number">24</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>)
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.lambda$func_241855_a$0(DecoratedFeature.java:<span class="hljs-number">19</span>)
<br />   at java.util.stream.Streams$StreamBuilderImpl.forEachRemaining(Streams.java:<span class="hljs-number">419</span>)
<br />   at java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:<span class="hljs-number">580</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>)
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.lambda$func_241855_a$0(DecoratedFeature.java:<span class="hljs-number">19</span>)
<br />   at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:<span class="hljs-number">184</span>)
<br />   at java.util.stream.IntPipeline$4$1.accept(IntPipeline.java:<span class="hljs-number">250</span>)
<br />   at java.util.stream.Streams$RangeIntSpliterator.forEachRemaining(Streams.java:<span class="hljs-number">110</span>)
<br />   at java.util.Spliterator$OfInt.forEachRemaining(Spliterator.java:<span class="hljs-number">693</span>)
<br />   at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:<span class="hljs-number">482</span>)
<br />   at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:<span class="hljs-number">472</span>)
<br />   at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:<span class="hljs-number">151</span>)
<br />   at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:<span class="hljs-number">174</span>)
<br />   at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:<span class="hljs-number">234</span>)
<br />   at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:<span class="hljs-number">418</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>)
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>)
<br />
<br />-- Feature --
<br />Details:
<br />   Id: minecraft:decorated
<br />   Config: &lt; DecoratedFeatureConfig [minecraft:decorated | [minecraft:count net.minecraft.world.gen.feature.FeatureSpreadConfig@2692ca85]] &gt;
<br />   Description: &lt; DecoratedFeature [minecraft:decorated] &gt;
<br />Stacktrace:
<br />   at net.minecraft.world.biome.Biome.func_242427_a(Biome.java:<span class="hljs-number">255</span>)
<br />
<br />-- Generation --
<br />Details:
<br />   CenterX: -<span class="hljs-number">1</span>
<br />   CenterZ: -<span class="hljs-number">2</span>
<br />   Seed: <span class="hljs-number">8503350945997188451</span>
<br />   Biome: net.minecraft.world.biome.Biome@99223ac
<br />Stacktrace:
<br />   at net.minecraft.world.gen.ChunkGenerator.func_230351_a<span class="hljs-number">_</span>(ChunkGenerator.java:<span class="hljs-number">192</span>)
<br />   at net.minecraft.world.chunk.ChunkStatus.lambda$static$9(ChunkStatus.java:<span class="hljs-number">77</span>)
<br />   at net.minecraft.world.chunk.ChunkStatus.doGenerationWork(ChunkStatus.java:<span class="hljs-number">198</span>)
<br />
<br />-- Chunk to be generated --
<br />Details:
<br />   Location: -<span class="hljs-number">1</span>,-<span class="hljs-number">2</span>
<br />   Position hash: -<span class="hljs-number">4294967297</span>
<br />   Generator: net.minecraft.world.gen.NoiseChunkGenerator@3c7efd58
<br />Stacktrace:
<br />   at net.minecraft.world.server.ChunkManager.lambda$null$18(ChunkManager.java:<span class="hljs-number">541</span>)
<br />   at com.mojang.datafixers.util.Either$Left.map(Either.java:<span class="hljs-number">38</span>)
<br />   at net.minecraft.world.server.ChunkManager.lambda$chunkGenerate$2<span class="hljs-number">0</span>(ChunkManager.java:<span class="hljs-number">539</span>)
<br />   at java.util.concurrent.CompletableFuture.uniCompose(CompletableFuture.java:<span class="hljs-number">966</span>)
<br />   at java.util.concurrent.CompletableFuture$UniCompose.tryFire(CompletableFuture.java:<span class="hljs-number">940</span>)
<br />   at java.util.concurrent.CompletableFuture$Completion.run(CompletableFuture.java:<span class="hljs-number">456</span>)
<br />   at net.minecraft.world.chunk.ChunkTaskPriorityQueueSorter.lambda$null$1(ChunkTaskPriorityQueueSorter.java:<span class="hljs-number">44</span>)
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.driveOne(DelegatedTaskExecutor.java:<span class="hljs-number">88</span>)
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.driveWhile(DelegatedTaskExecutor.java:<span class="hljs-number">132</span>)
<br />
<br />-- Affected level --
<br />Details:
<br />   All players: <span class="hljs-number">1</span> total; [ServerPlayerEntity[<span class="hljs-string">'Dev'</span>/<span class="hljs-number">168</span>, l=<span class="hljs-string">'ServerLevel[New World]'</span>, <span class="hljs-keyword">x</span>=<span class="hljs-number">10.88</span>, <span class="hljs-keyword">y</span>=<span class="hljs-number">94.06</span>, z=-<span class="hljs-number">6.25</span>]]
<br />   Chunk stats: ServerChunkCache: <span class="hljs-number">625</span>
<br />   Level dimension: minecraft:the_nether
<br />   Derived: true
<br />   Level spawn location: World: (<span class="hljs-number">1</span>,<span class="hljs-number">70</span>,<span class="hljs-number">8</span>), Chunk: (at <span class="hljs-number">1</span>,<span class="hljs-number">4</span>,<span class="hljs-number">8</span> in <span class="hljs-number">0</span>,<span class="hljs-number">0</span>; contains blocks <span class="hljs-number">0</span>,<span class="hljs-number">0</span>,<span class="hljs-number">0</span> to <span class="hljs-number">15</span>,<span class="hljs-number">255</span>,<span class="hljs-number">15</span>), Region: (<span class="hljs-number">0</span>,<span class="hljs-number">0</span>; contains chunks <span class="hljs-number">0</span>,<span class="hljs-number">0</span> to <span class="hljs-number">31</span>,<span class="hljs-number">31</span>, blocks <span class="hljs-number">0</span>,<span class="hljs-number">0</span>,<span class="hljs-number">0</span> to <span class="hljs-number">511</span>,<span class="hljs-number">255</span>,<span class="hljs-number">511</span>)
<br />   Level <span class="hljs-keyword">time</span>: <span class="hljs-number">433</span> game <span class="hljs-keyword">time</span>, <span class="hljs-number">433</span> day <span class="hljs-keyword">time</span>
<br />   Level name: New World
<br />   Level game mode: Game mode: creative (ID <span class="hljs-number">1</span>). Hardcore: false. Cheats: true
<br />   Level weather: Rain <span class="hljs-keyword">time</span>: <span class="hljs-number">64640</span> (now: false), thunder <span class="hljs-keyword">time</span>: <span class="hljs-number">55925</span> (now: false)
<br />   Known server brands: forge
<br />   Level was modded: true
<br />   Level storage version: <span class="hljs-number">0x04ABD</span> - Anvil
<br />Stacktrace:
<br />   at net.minecraft.server.MinecraftServer.updateTimeLightAndEntities(MinecraftServer.java:<span class="hljs-number">883</span>)
<br />   at net.minecraft.server.MinecraftServer.tick(MinecraftServer.java:<span class="hljs-number">816</span>)
<br />   at net.minecraft.server.integrated.IntegratedServer.tick(IntegratedServer.java:<span class="hljs-number">86</span>)
<br />   at net.minecraft.server.MinecraftServer.func_240802_v<span class="hljs-number">_</span>(MinecraftServer.java:<span class="hljs-number">659</span>)
<br />   at net.minecraft.server.MinecraftServer.lambda$func_240784_a_$0(MinecraftServer.java:<span class="hljs-number">230</span>)
<br />   at java.lang.Thread.run(Thread.java:<span class="hljs-number">748</span>)
<br />
<br />-- System Details --
<br />Details:
<br />   Minecraft Version: <span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>
<br />   Minecraft Version ID: <span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>
<br />   Operating System: Windows <span class="hljs-number">10</span> (amd64) version <span class="hljs-number">10.0</span>
<br />   Java Version: <span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>, Oracle Corporation
<br />   Java VM Version: Java HotSpot(TM) <span class="hljs-number">64</span>-Bit Server VM (mixed mode), Oracle Corporation
<br />   Memory: <span class="hljs-number">748656856</span> bytes (<span class="hljs-number">713</span> MB) / <span class="hljs-number">3538944000</span> bytes (<span class="hljs-number">3375</span> MB) up to <span class="hljs-number">7594835968</span> bytes (<span class="hljs-number">7243</span> MB)
<br />   CPUs: <span class="hljs-number">12</span>
<br />   JVM Flags: <span class="hljs-number">1</span> total; -XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump
<br />   ModLauncher: <span class="hljs-number">6.1</span>.<span class="hljs-number">1</span>+<span class="hljs-number">74</span>+master.<span class="hljs-number">966</span>c698
<br />   ModLauncher launch target: fmluserdevclient
<br />   ModLauncher naming: mcp
<br />   ModLauncher services: 
<br />      <span class="hljs-regexp">/mixin-0.8.jar mixin PLUGINSERVICE 
<br />      /eventbus</span>-<span class="hljs-number">3.0</span>.<span class="hljs-number">3</span>-service.jar eventbus PLUGINSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar object_holder_definalize PLUGINSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar runtime_enum_extender PLUGINSERVICE 
<br />      /accesstransformers-<span class="hljs-number">2.2</span>.<span class="hljs-number">0</span>-shadowed.jar accesstransformer PLUGINSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar capability_inject_definalize PLUGINSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar runtimedistcleaner PLUGINSERVICE 
<br />      /mixin-<span class="hljs-number">0</span>.<span class="hljs-number">8</span>.jar mixin TRANSFORMATIONSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar fml TRANSFORMATIONSERVICE 
<br />   FML: <span class="hljs-number">33.0</span>
<br />   Forge: net.minecraftforge:<span class="hljs-number">33.0</span>.<span class="hljs-number">22</span>
<br />   FML Language Providers: 
<br />      javafml@33.<span class="hljs-number">0</span>
<br />      minecraft@1
<br />   Mod List: 
<br />      client-extra.jar Minecraft {minecraft@1.<span class="hljs-number">16.2</span> DONE}
<br />      forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-recomp.jar Forge {forge@33.<span class="hljs-number">0</span>.<span class="hljs-number">22</span> DONE}
<br />      main Feature Blame {feature_blame@NONE DONE}
<br />   Player Count: <span class="hljs-number">1</span> / <span class="hljs-number">8</span>; [ServerPlayerEntity[<span class="hljs-string">'Dev'</span>/<span class="hljs-number">168</span>, l=<span class="hljs-string">'ServerLevel[New World]'</span>, <span class="hljs-keyword">x</span>=<span class="hljs-number">10.88</span>, <span class="hljs-keyword">y</span>=<span class="hljs-number">94.06</span>, z=-<span class="hljs-number">6.25</span>]]
<br />   Data Packs: vanilla, mod:forge (incompatible), mod:feature_blame (incompatible)
<br />   Type: Integrated Server (map_client.txt)
<br />   Is Modded: Definitely; Client brand changed to <span class="hljs-string">'forge'</span></pre>
</div>
<p>&nbsp;</p>
<p><span style="font-size: 18px;"><strong>HOWEVER,</strong></span></p>
<p>with <strong>Blame</strong> on, a<strong> Blame Report</strong> is inserted at the bottom of the crashlog that says it is the <span style="text-decoration: underline;">minecraft:ore_quartz_nether</span> feature in <span style="text-decoration: underline;">minecraft:warped_forest</span> biome that is causing the crash. And it even prints out the JSON format of the feature and we can see that the <span style="text-decoration: underline;">decorator</span> <span style="text-decoration: underline;">config</span>'s <span style="text-decoration: underline;">maximum</span> is set to 0 which is causing the "<span style="text-decoration: underline;">bound must be positive</span>" crash! So much valuable info!!!</p>
<div class="spoiler">
<pre>---- Minecraft Crash Report ----
<br /><span class="hljs-regexp">//</span> My bad.
<br />
<br />Time: <span class="hljs-number">9</span>/<span class="hljs-number">3</span>/<span class="hljs-number">20</span> <span class="hljs-number">2</span>:<span class="hljs-number">23</span> PM
<br />Description: Feature placement
<br />
<br />java.lang.IllegalArgumentException: bound must be positive
<br />   at java.util.Random.nextInt(Random.java:<span class="hljs-number">388</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at net.minecraft.world.gen.placement.RangePlacement.getPositions(RangePlacement.java:<span class="hljs-number">16</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.placement.RangePlacement.getPositions(RangePlacement.java:<span class="hljs-number">8</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.placement.SimplePlacement.func_241857_a(SimplePlacement.java:<span class="hljs-number">15</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.placement.ConfiguredPlacement.func_242876_a(ConfiguredPlacement.java:<span class="hljs-number">24</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>) ~[?:?] {re:mixin,re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.lambda$func_241855_a$0(DecoratedFeature.java:<span class="hljs-number">19</span>) ~[?:?] {re:classloading}
<br />   at java.util.stream.Streams$StreamBuilderImpl.forEachRemaining(Streams.java:<span class="hljs-number">419</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:<span class="hljs-number">580</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>) ~[?:?] {re:mixin,re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.lambda$func_241855_a$0(DecoratedFeature.java:<span class="hljs-number">19</span>) ~[?:?] {re:classloading}
<br />   at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:<span class="hljs-number">184</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.IntPipeline$4$1.accept(IntPipeline.java:<span class="hljs-number">250</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.Streams$RangeIntSpliterator.forEachRemaining(Streams.java:<span class="hljs-number">110</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.Spliterator$OfInt.forEachRemaining(Spliterator.java:<span class="hljs-number">693</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:<span class="hljs-number">482</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:<span class="hljs-number">472</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:<span class="hljs-number">151</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:<span class="hljs-number">174</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:<span class="hljs-number">234</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:<span class="hljs-number">418</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>) ~[?:?] {re:mixin,re:classloading}
<br />   at net.minecraft.world.biome.Biome.func_242427_a(Biome.java:<span class="hljs-number">255</span>) ~[?:?] {re:mixin,re:classloading,pl:mixin:APP:blame.mixins.json:BiomeMixin,pl:mixin:A}
<br />   at net.minecraft.world.gen.ChunkGenerator.func_230351_a<span class="hljs-number">_</span>(ChunkGenerator.java:<span class="hljs-number">192</span>) ~[?:?] {re:mixin,re:classloading}
<br />   at net.minecraft.world.chunk.ChunkStatus.lambda$static$9(ChunkStatus.java:<span class="hljs-number">77</span>) ~[?:?] {re:classloading,pl:accesstransformer:B}
<br />   at net.minecraft.world.chunk.ChunkStatus.doGenerationWork(ChunkStatus.java:<span class="hljs-number">198</span>) ~[?:?] {re:classloading,pl:accesstransformer:B}
<br />   at net.minecraft.world.server.ChunkManager.lambda$null$18(ChunkManager.java:<span class="hljs-number">541</span>) ~[?:?] {re:classloading}
<br />   at com.mojang.datafixers.util.Either$Left.map(Either.java:<span class="hljs-number">38</span>) ~[datafixerupper-<span class="hljs-number">4.0</span>.<span class="hljs-number">26</span>.jar:?] {re:classloading,re:classloading,re:classloading}
<br />   at net.minecraft.world.server.ChunkManager.lambda$chunkGenerate$2<span class="hljs-number">0</span>(ChunkManager.java:<span class="hljs-number">539</span>) ~[?:?] {re:classloading}
<br />   at java.util.concurrent.CompletableFuture.uniCompose(CompletableFuture.java:<span class="hljs-number">966</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.CompletableFuture$UniCompose.tryFire(CompletableFuture.java:<span class="hljs-number">940</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.CompletableFuture$Completion.run(CompletableFuture.java:<span class="hljs-number">456</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at net.minecraft.world.chunk.ChunkTaskPriorityQueueSorter.lambda$null$1(ChunkTaskPriorityQueueSorter.java:<span class="hljs-number">44</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.driveOne(DelegatedTaskExecutor.java:<span class="hljs-number">88</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.driveWhile(DelegatedTaskExecutor.java:<span class="hljs-number">132</span>) ~[?:?] {re:classloading}
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.run(DelegatedTaskExecutor.java:<span class="hljs-number">100</span>) ~[?:?] {re:classloading}
<br />   at java.util.concurrent.ForkJoinTask$RunnableExecuteAction.exec(ForkJoinTask.java:<span class="hljs-number">1402</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:<span class="hljs-number">289</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.ForkJoinPool$WorkQueue.runTask(ForkJoinPool.java:<span class="hljs-number">1056</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:<span class="hljs-number">1692</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />   at java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:<span class="hljs-number">157</span>) ~[?:<span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>] {}
<br />
<br />
<br />A detailed walkthrough of the error, its code path <span class="hljs-keyword">and</span> all known details is as follows:
<br />---------------------------------------------------------------------------------------
<br />
<br />-- Head --
<br />Thread: Server thread
<br />Stacktrace:
<br />   at java.util.Random.nextInt(Random.java:<span class="hljs-number">388</span>)
<br />   at net.minecraft.world.gen.placement.RangePlacement.getPositions(RangePlacement.java:<span class="hljs-number">16</span>)
<br />   at net.minecraft.world.gen.placement.RangePlacement.getPositions(RangePlacement.java:<span class="hljs-number">8</span>)
<br />   at net.minecraft.world.gen.placement.SimplePlacement.func_241857_a(SimplePlacement.java:<span class="hljs-number">15</span>)
<br />   at net.minecraft.world.gen.placement.ConfiguredPlacement.func_242876_a(ConfiguredPlacement.java:<span class="hljs-number">24</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>)
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.lambda$func_241855_a$0(DecoratedFeature.java:<span class="hljs-number">19</span>)
<br />   at java.util.stream.Streams$StreamBuilderImpl.forEachRemaining(Streams.java:<span class="hljs-number">419</span>)
<br />   at java.util.stream.ReferencePipeline$Head.forEach(ReferencePipeline.java:<span class="hljs-number">580</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>)
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.lambda$func_241855_a$0(DecoratedFeature.java:<span class="hljs-number">19</span>)
<br />   at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:<span class="hljs-number">184</span>)
<br />   at java.util.stream.IntPipeline$4$1.accept(IntPipeline.java:<span class="hljs-number">250</span>)
<br />   at java.util.stream.Streams$RangeIntSpliterator.forEachRemaining(Streams.java:<span class="hljs-number">110</span>)
<br />   at java.util.Spliterator$OfInt.forEachRemaining(Spliterator.java:<span class="hljs-number">693</span>)
<br />   at java.util.stream.AbstractPipeline.copyInto(AbstractPipeline.java:<span class="hljs-number">482</span>)
<br />   at java.util.stream.AbstractPipeline.wrapAndCopyInto(AbstractPipeline.java:<span class="hljs-number">472</span>)
<br />   at java.util.stream.ForEachOps$ForEachOp.evaluateSequential(ForEachOps.java:<span class="hljs-number">151</span>)
<br />   at java.util.stream.ForEachOps$ForEachOp$OfRef.evaluateSequential(ForEachOps.java:<span class="hljs-number">174</span>)
<br />   at java.util.stream.AbstractPipeline.evaluate(AbstractPipeline.java:<span class="hljs-number">234</span>)
<br />   at java.util.stream.ReferencePipeline.forEach(ReferencePipeline.java:<span class="hljs-number">418</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">18</span>)
<br />   at net.minecraft.world.gen.feature.DecoratedFeature.func_241855_a(DecoratedFeature.java:<span class="hljs-number">11</span>)
<br />   at net.minecraft.world.gen.feature.ConfiguredFeature.func_242765_a(ConfiguredFeature.java:<span class="hljs-number">52</span>)
<br />
<br />-- Feature --
<br />Details:
<br />   Id: minecraft:decorated
<br />   Config: &lt; DecoratedFeatureConfig [minecraft:decorated | [minecraft:count net.minecraft.world.gen.feature.FeatureSpreadConfig@79f84b39]] &gt;
<br />   Description: &lt; DecoratedFeature [minecraft:decorated] &gt;
<br />Stacktrace:
<br />   at net.minecraft.world.biome.Biome.func_242427_a(Biome.java:<span class="hljs-number">255</span>)
<br />
<br />-- Generation --
<br />Details:
<br />   CenterX: -<span class="hljs-number">1</span>
<br />   CenterZ: -<span class="hljs-number">2</span>
<br />   Seed: <span class="hljs-number">8503350945997188451</span>
<br />   Biome: net.minecraft.world.biome.Biome@1c0cf193
<br />Stacktrace:
<br />   at net.minecraft.world.gen.ChunkGenerator.func_230351_a<span class="hljs-number">_</span>(ChunkGenerator.java:<span class="hljs-number">192</span>)
<br />   at net.minecraft.world.chunk.ChunkStatus.lambda$static$9(ChunkStatus.java:<span class="hljs-number">77</span>)
<br />   at net.minecraft.world.chunk.ChunkStatus.doGenerationWork(ChunkStatus.java:<span class="hljs-number">198</span>)
<br />
<br />-- Chunk to be generated --
<br />Details:
<br />   Location: -<span class="hljs-number">1</span>,-<span class="hljs-number">2</span>
<br />   Position hash: -<span class="hljs-number">4294967297</span>
<br />   Generator: net.minecraft.world.gen.NoiseChunkGenerator@6e9382ce
<br />Stacktrace:
<br />   at net.minecraft.world.server.ChunkManager.lambda$null$18(ChunkManager.java:<span class="hljs-number">541</span>)
<br />   at com.mojang.datafixers.util.Either$Left.map(Either.java:<span class="hljs-number">38</span>)
<br />   at net.minecraft.world.server.ChunkManager.lambda$chunkGenerate$2<span class="hljs-number">0</span>(ChunkManager.java:<span class="hljs-number">539</span>)
<br />   at java.util.concurrent.CompletableFuture.uniCompose(CompletableFuture.java:<span class="hljs-number">966</span>)
<br />   at java.util.concurrent.CompletableFuture$UniCompose.tryFire(CompletableFuture.java:<span class="hljs-number">940</span>)
<br />   at java.util.concurrent.CompletableFuture$Completion.run(CompletableFuture.java:<span class="hljs-number">456</span>)
<br />   at net.minecraft.world.chunk.ChunkTaskPriorityQueueSorter.lambda$null$1(ChunkTaskPriorityQueueSorter.java:<span class="hljs-number">44</span>)
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.driveOne(DelegatedTaskExecutor.java:<span class="hljs-number">88</span>)
<br />   at net.minecraft.util.concurrent.DelegatedTaskExecutor.driveWhile(DelegatedTaskExecutor.java:<span class="hljs-number">132</span>)
<br />
<br />-- Affected level --
<br />Details:
<br />   All players: <span class="hljs-number">1</span> total; [ServerPlayerEntity[<span class="hljs-string">'Dev'</span>/<span class="hljs-number">187</span>, l=<span class="hljs-string">'ServerLevel[New World]'</span>, <span class="hljs-keyword">x</span>=<span class="hljs-number">10.88</span>, <span class="hljs-keyword">y</span>=<span class="hljs-number">94.06</span>, z=-<span class="hljs-number">6.25</span>]]
<br />   Chunk stats: ServerChunkCache: <span class="hljs-number">625</span>
<br />   Level dimension: minecraft:the_nether
<br />   Derived: true
<br />   Level spawn location: World: (<span class="hljs-number">1</span>,<span class="hljs-number">70</span>,<span class="hljs-number">8</span>), Chunk: (at <span class="hljs-number">1</span>,<span class="hljs-number">4</span>,<span class="hljs-number">8</span> in <span class="hljs-number">0</span>,<span class="hljs-number">0</span>; contains blocks <span class="hljs-number">0</span>,<span class="hljs-number">0</span>,<span class="hljs-number">0</span> to <span class="hljs-number">15</span>,<span class="hljs-number">255</span>,<span class="hljs-number">15</span>), Region: (<span class="hljs-number">0</span>,<span class="hljs-number">0</span>; contains chunks <span class="hljs-number">0</span>,<span class="hljs-number">0</span> to <span class="hljs-number">31</span>,<span class="hljs-number">31</span>, blocks <span class="hljs-number">0</span>,<span class="hljs-number">0</span>,<span class="hljs-number">0</span> to <span class="hljs-number">511</span>,<span class="hljs-number">255</span>,<span class="hljs-number">511</span>)
<br />   Level <span class="hljs-keyword">time</span>: <span class="hljs-number">881</span> game <span class="hljs-keyword">time</span>, <span class="hljs-number">881</span> day <span class="hljs-keyword">time</span>
<br />   Level name: New World
<br />   Level game mode: Game mode: creative (ID <span class="hljs-number">1</span>). Hardcore: false. Cheats: true
<br />   Level weather: Rain <span class="hljs-keyword">time</span>: <span class="hljs-number">64192</span> (now: false), thunder <span class="hljs-keyword">time</span>: <span class="hljs-number">55477</span> (now: false)
<br />   Known server brands: forge
<br />   Level was modded: true
<br />   Level storage version: <span class="hljs-number">0x04ABD</span> - Anvil
<br />Stacktrace:
<br />   at net.minecraft.server.MinecraftServer.updateTimeLightAndEntities(MinecraftServer.java:<span class="hljs-number">883</span>)
<br />   at net.minecraft.server.MinecraftServer.tick(MinecraftServer.java:<span class="hljs-number">816</span>)
<br />   at net.minecraft.server.integrated.IntegratedServer.tick(IntegratedServer.java:<span class="hljs-number">86</span>)
<br />   at net.minecraft.server.MinecraftServer.func_240802_v<span class="hljs-number">_</span>(MinecraftServer.java:<span class="hljs-number">659</span>)
<br />   at net.minecraft.server.MinecraftServer.lambda$func_240784_a_$0(MinecraftServer.java:<span class="hljs-number">230</span>)
<br />   at java.lang.Thread.run(Thread.java:<span class="hljs-number">748</span>)
<br />
<br />-- System Details --
<br />Details:
<br />   Minecraft Version: <span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>
<br />   Minecraft Version ID: <span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>
<br />   Operating System: Windows <span class="hljs-number">10</span> (amd64) version <span class="hljs-number">10.0</span>
<br />   Java Version: <span class="hljs-number">1.8</span>.<span class="hljs-number">0_251</span>, Oracle Corporation
<br />   Java VM Version: Java HotSpot(TM) <span class="hljs-number">64</span>-Bit Server VM (mixed mode), Oracle Corporation
<br />   Memory: <span class="hljs-number">750911496</span> bytes (<span class="hljs-number">716</span> MB) / <span class="hljs-number">3622830080</span> bytes (<span class="hljs-number">3455</span> MB) up to <span class="hljs-number">7594835968</span> bytes (<span class="hljs-number">7243</span> MB)
<br />   CPUs: <span class="hljs-number">12</span>
<br />   JVM Flags: <span class="hljs-number">1</span> total; -XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump
<br />   ModLauncher: <span class="hljs-number">6.1</span>.<span class="hljs-number">1</span>+<span class="hljs-number">74</span>+master.<span class="hljs-number">966</span>c698
<br />   ModLauncher launch target: fmluserdevclient
<br />   ModLauncher naming: mcp
<br />   ModLauncher services: 
<br />      <span class="hljs-regexp">/mixin-0.8.jar mixin PLUGINSERVICE 
<br />      /eventbus</span>-<span class="hljs-number">3.0</span>.<span class="hljs-number">3</span>-service.jar eventbus PLUGINSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar object_holder_definalize PLUGINSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar runtime_enum_extender PLUGINSERVICE 
<br />      /accesstransformers-<span class="hljs-number">2.2</span>.<span class="hljs-number">0</span>-shadowed.jar accesstransformer PLUGINSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar capability_inject_definalize PLUGINSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar runtimedistcleaner PLUGINSERVICE 
<br />      /mixin-<span class="hljs-number">0</span>.<span class="hljs-number">8</span>.jar mixin TRANSFORMATIONSERVICE 
<br />      /forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-launcher.jar fml TRANSFORMATIONSERVICE 
<br />   FML: <span class="hljs-number">33.0</span>
<br />   Forge: net.minecraftforge:<span class="hljs-number">33.0</span>.<span class="hljs-number">22</span>
<br />   FML Language Providers: 
<br />      javafml@33.<span class="hljs-number">0</span>
<br />      minecraft@1
<br />   Mod List: 
<br />      client-extra.jar Minecraft {minecraft@1.<span class="hljs-number">16.2</span> DONE}
<br />      forge-<span class="hljs-number">1.16</span>.<span class="hljs-number">2</span>-<span class="hljs-number">33.0</span>.<span class="hljs-number">22_</span>mapped_snapshot_20200723-<span class="hljs-number">1.16</span>.<span class="hljs-number">1</span>-recomp.jar Forge {forge@33.<span class="hljs-number">0</span>.<span class="hljs-number">22</span> DONE}
<br />      main Blame! {blame@NONE DONE}
<br />   
<br />****************** Blame Report ******************: 
<br />
<br /> ConfiguredFeature Registry Name : minecraft:ore_quartz_nether

<br /> Biome Registry Name : minecraft:warped_forest
<br />
<br /> JSON info : {
<br />  <span class="hljs-string">"config"</span>: {
<br />    <span class="hljs-string">"feature"</span>: {
<br />      <span class="hljs-string">"config"</span>: {
<br />        <span class="hljs-string">"feature"</span>: {
<br />          <span class="hljs-string">"config"</span>: {
<br />            <span class="hljs-string">"feature"</span>: {
<br />              <span class="hljs-string">"config"</span>: {
<br />                <span class="hljs-string">"target"</span>: {
<br />                  <span class="hljs-string">"block"</span>: <span class="hljs-string">"minecraft:netherrack"</span>,
<br />                  <span class="hljs-string">"predicate_type"</span>: <span class="hljs-string">"minecraft:block_match"</span>
<br />                },
<br />                <span class="hljs-string">"state"</span>: {
<br />                  <span class="hljs-string">"Name"</span>: <span class="hljs-string">"minecraft:nether_quartz_ore"</span>
<br />                },
<br />                <span class="hljs-string">"size"</span>: <span class="hljs-number">14</span>
<br />              },
<br />              <span class="hljs-string">"type"</span>: <span class="hljs-string">"minecraft:ore"</span>
<br />            },
<br />            <span class="hljs-string">"decorator"</span>: {
<br />              <span class="hljs-string">"config"</span>: {
<br />                <span class="hljs-string">"bottom_offset"</span>: <span class="hljs-number">10</span>,
<br />                <span class="hljs-string">"top_offset"</span>: <span class="hljs-number">20</span>,
<br />                <span class="hljs-string">"maximum"</span>: <span class="hljs-number">0</span>
<br />              },
<br />              <span class="hljs-string">"type"</span>: <span class="hljs-string">"minecraft:range"</span>
<br />            }
<br />          },
<br />          <span class="hljs-string">"type"</span>: <span class="hljs-string">"minecraft:decorated"</span>
<br />        },
<br />        <span class="hljs-string">"decorator"</span>: {
<br />          <span class="hljs-string">"config"</span>: {},
<br />          <span class="hljs-string">"type"</span>: <span class="hljs-string">"minecraft:square"</span>
<br />        }
<br />      },
<br />      <span class="hljs-string">"type"</span>: <span class="hljs-string">"minecraft:decorated"</span>
<br />    },
<br />    <span class="hljs-string">"decorator"</span>: {
<br />      <span class="hljs-string">"config"</span>: {
<br />        <span class="hljs-string">"count"</span>: <span class="hljs-number">16</span>
<br />      },
<br />      <span class="hljs-string">"type"</span>: <span class="hljs-string">"minecraft:count"</span>
<br />    }
<br />  },
<br />  <span class="hljs-string">"type"</span>: <span class="hljs-string">"minecraft:decorated"</span>
<br />}
<br />
<br />
<br />   Player Count: <span class="hljs-number">1</span> / <span class="hljs-number">8</span>; [ServerPlayerEntity[<span class="hljs-string">'Dev'</span>/<span class="hljs-number">187</span>, l=<span class="hljs-string">'ServerLevel[New World]'</span>, <span class="hljs-keyword">x</span>=<span class="hljs-number">10.88</span>, <span class="hljs-keyword">y</span>=<span class="hljs-number">94.06</span>, z=-<span class="hljs-number">6.25</span>]]
<br />   Data Packs: vanilla, mod:forge (incompatible), mod:blame (incompatible), file/testdatapack (incompatible)
<br />   Type: Integrated Server (map_client.txt)
<br />   Is Modded: Definitely; Client brand changed to <span class="hljs-string">'forge'</span></pre>
</div>
<p>&nbsp;</p>
<p><strong>Current capabilities of this mod:</strong></p>
<p>&nbsp;</p>
<p>&nbsp; - Prints extra detail for features or structures that crashes during worldgen.</p>
<p>&nbsp;</p>
<p>&nbsp; - Logs all ConfiguredFeatures, ConfiguredStructures, ConfiguredCarvers that are not registered!</p>
<p>&nbsp; &nbsp; (Mods that don't register the stuff will cause other mod's stuff to break and not spawn during worldgen)</p>
<p>&nbsp;</p>
<p>&nbsp; - Detect if DynamicRegistry is loaded way too early by another mod!</p>
<p>&nbsp; &nbsp; (Doing so causes all other mod's registered worldgen stuff to blow up such as "Unknown Biome ID" issues)</p>
<p>&nbsp;</p>
<p>&nbsp;- Logs out exactly which worldgen JSON file is broken from any mod or datapack and shows its JSON too!&nbsp;</p>
<p>&nbsp;</p>
<p>&nbsp;- Will log out if a mod or datapack tries to access a non-existent nbt file for easier debugging!</p>
<p><img src="https://media.forgecdn.net/attachments/315/939/untitled.png" alt="Missing nbt file Blame report" width="590" height="132" /></p>
<h3><br />Try out <strong>Blame</strong> today! It's only&nbsp;<span style="text-decoration: underline; font-size: 36px;"><em><strong>free</strong></em></span>!!!!</h3>
<p>&nbsp;</p>
<p><br />Comment if there's any sort of crash report that should get more info and I'll see what I can do. Though with rendering, I have zero knowledge on that so I dunno if I can help with those kinds of crash reports lmao.</p>
<p><br />(Also if&nbsp; you are a modder, feel free to PR extra stuff into <strong>Blame</strong> to improve more crash reports or add even more info! Click Source above to go to the GitHub page and you can fork to make PRs. Just ask me for help if you get stuck!)</p>
<p style="text-align: center;"><br /><span style="font-size: 18px;"><strong>Discord Link for my channel about my mods! :&nbsp;<a href="https://discord.gg/4eHptfV" rel="nofollow">https://discord.gg/4eHptfV</a></strong></span><br /><br /></p>