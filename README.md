# miNEMcraft
A plugin for bukkit/spigot.

[ ![Codeship Status for planethouki/miNEMcraft](https://app.codeship.com/projects/1f434340-b5e4-0135-02bd-0abdbc6c21b9/status?branch=master)](https://app.codeship.com/projects/258518)

## 進捗
* ハーベスト、マイニング仮完成

## やりたいこと
* ハーベスト 作物を収穫するとXEM（もしくはモザイク）がもらえる
* マイニング 地下を掘ると（ry
* アポスティーユ 飼いならしたモブの所有権を証明する

## アーキテクチャ的な
* testnet
* 同じサーバー上でspigotとNISを動かす
* やりとりは全てチェーンに乗せる
* 与えられるXEMはすべて鯖主が用意する

## コマンド
* /mnc balance 残高を確認する
* /mnc resister <Address> アドレスを登録する（上書き）
* /mnc generate アドレスを生成する。