package wn.monopoly.util

import scalafx.scene.media.{AudioClip, Media, MediaPlayer}
import scalafx.scene.control.Button
import scalafx.scene.image.Image
import scalafx.scene.media.MediaPlayer.Indefinite

object MultimediaHandler{

  var playMusicStatus: Boolean = false //store a boolean to know current music status
  var muted: Boolean = false //store a boolean to know current sound effect status
  private val music = new Media(getClass.getResource("/sounds/bgMusic.mp3").toString)

  private val bgMusicPlayer = new MediaPlayer(music){ //create a media player to play bg music
    cycleCount = Indefinite //repeat infinite
  }

  def createImage(fileName: String): Image = { //receive image file name to create image
    new Image (getClass.getResourceAsStream("/images/%s".format(fileName)))
  }

  private def playSound(fileName: String): Unit = { //
    if(!muted) {
      val sound = new AudioClip(getClass.getResource(fileName).toString)
      sound.play()
    }
  }

  def playRollDiceSound(): Unit = {
    playSound("/sounds/rollingDice.mp3")
  }

  def playPaySuccessSound(): Unit = {
    playSound("/sounds/paySuccess.mp3")
  }

  def playPayFailSound(): Unit = {
    playSound("/sounds/payFail.mp3")
  }

  def playDrawCardSound(): Unit = {
    playSound("/sounds/drawingCard.mp3")
  }

  def playButtonClickSound(): Unit = {
    playSound("/sounds/buttonClick.mp3")
  }

  def playButtonHoverSound(): Unit = {
    playSound("/sounds/buttonHover.mp3")
  }

  def setHoverSound(button: Button): Unit = { //play sound when the button is hovered
    button.onMouseEntered = e => MultimediaHandler.playButtonHoverSound()
  }

  def playMoveTokenSound(): Unit = { //move token sound
    playSound("/sounds/moveToken.mp3")
  }

  def playBgMusic(): Unit = {
    playMusicStatus = true
    bgMusicPlayer.play()
  }

  def setVolume(status: Boolean): Unit = {
    if (status){
      playMusicStatus = true
      bgMusicPlayer.setVolume(1.0)
    } else{
      playMusicStatus = false
      bgMusicPlayer.setVolume(0)
    }

  }

  def setMute(status: Boolean) ={
    muted = status
  }
}