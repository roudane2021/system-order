import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';

// Déclaration pour éviter les erreurs TypeScript avec les API Web natives
const { webkitSpeechRecognition } = window as any;

@Injectable({
  providedIn: 'root'
})
export class VoiceService {
  private recognition: any;
  private synth = window.speechSynthesis;

  // Permet au composant d'écouter le texte final détecté
  public textDetected$ = new Subject<String>();

  constructor() {
    if (webkitSpeechRecognition) {
      this.recognition = new webkitSpeechRecognition();
      this.recognition.continuous = false; // S'arrête quand l'utilisateur cesse de parler
      this.recognition.lang = 'fr-FR';     // Langue française
      this.recognition.interimResults = false;

      this.recognition.onresult = (event: any) => {
        const speechToText = event.results[0][0].transcript;
        this.textDetected$.next(speechToText);
      };
    } else {
      console.warn("La reconnaissance vocale n'est pas supportée par ce navigateur.");
    }
  }

  // 1. Commencer à écouter l'utilisateur
  startListening() {
    if (this.recognition) {
      this.recognition.start();
    }
  }

  // 2. Faire parler le navigateur (réponse de l'IA)
  speak(text: string) {
    if (this.synth) {
      // Annule une éventuelle voix en cours
      this.synth.cancel();

      const utterance = new SpeechSynthesisUtterance(text);
      utterance.lang = 'fr-FR'; // Voix française

      // Optionnel : Choisir une voix spécifique si disponible
      const voices = this.synth.getVoices();
      const frenchVoice = voices.find(v => v.lang.startsWith('fr'));
      if (frenchVoice) utterance.voice = frenchVoice;

      this.synth.speak(utterance);
    }
  }
}
