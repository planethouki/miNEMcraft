# miNEMcraft
A plugin for bukkit/spigot.

[ ![Codeship Status for planethouki/miNEMcraft](https://app.codeship.com/projects/1f434340-b5e4-0135-02bd-0abdbc6c21b9/status?branch=master)](https://app.codeship.com/projects/258518)


## TODO
* 秘密鍵ファイルの安全な取り扱い
* ListenerのところのObservableの扱いがよくわかってない
* トランザクションが失敗したときの処理

## 特徴
* ハーベスト 作物を収穫するとXEMがもらえる
* マイニング 地下を掘るとXEMがもらえる
* XEMを他プレイヤーに送れる
* アカウントはサーバー側で管理
* 進捗情報をブロックチェーンに書き込み


## コマンド
* /mnc balance 残高を確認する
* /mnc address アドレスを表示する
* /mnc send &lt;player&gt; [amount] 送金する
* /mnc reload 設定を再読み込み


## 環境
* spigot 1.12.2
* java 1.8.0_181

## パッケージio.nem.sdk～のソースについて
https://github.com/nemtech/nem2-sdk-java からコピーしています。

nem2-sdk-javaの依存しているパッケージがspigotにある場合、そちらが優先されるようです。

そのため、いくつかのメソッドが動かない現象が起きています。

これを回避するために、プロジェクト内に修正したソースを保持しています。

* 修正リスト
* io.nem.sdk.model.account.Account.java
* io.nem.sdk.infrastructure.AccountHttp.java


## miNEMcarft Build

download or clone this repository.

open in eclipse

プロジェクトを右クリック > 実行(R) > Maven install

in "target" folder, there is miNEMcraft.jar.


## Minecraft Server Install

##### Download

Download BuildTools.jar

https://hub.spigotmc.org/jenkins/job/BuildTools/

##### Build

Build bukkit/spigot


```
java -jar BuildTools.jar --rev 1.12.2
```

take a few minutes...

##### run

run command

```
java -Xms1G -Xmx1G -XX:+UseConcMarkSweepGC -jar spigot-1.12.2.jar
```

will be fail. agreement eura.txt

```
eula=true
```

run command again. this massage shows plain server running.

```
[11:36:16 INFO]: Done (2.972s)! For help, type "help" or "?"
```

stop

```
> stop
```

##### Add Plugin

you will find out "plugins" folder.

put miNEMcraft.jar in the folder

run command again. you can use miNEMcraft commands.


## Play minecraft

buy minecraft java edition

https://minecraft.net/ja-jp/