import { Component, OnInit, OnDestroy } from '@angular/core';
import { VoiceService } from './services/voice.service';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-voice-agent',
  template: `
    <div class="voice-agent-container">
      <button (click)="listen()" [disabled]="isListening">
        {{ isListening ? 'Je vous écoute...' : 'Parler à l\\'agent' }}
      </button>

      <p><strong>Vous avez dit :</strong> {{ userQuestion }}</p>
      <p><strong>IA :</strong> {{ aiResponse }}</p>
    </div>
  `
})
export class InventoryComponent implements OnInit, OnDestroy {
  userQuestion = '';
  aiResponse = '';
  isListening = false;
  private sub!: Subscription;

  constructor(private voiceService: VoiceService, private http: HttpClient) {}

  ngOnInit() {
    // Écouter dès que le service détecte une phrase complète
    this.sub = this.voiceService.textDetected$.subscribe((text: any) => {
      this.isListening = false;
      this.userQuestion = text;
      this.askSpringAgent(text);
    });
  }

  listen() {
    this.isListening = true;
    this.aiResponse = '';
    this.voiceService.startListening();
  }

  askSpringAgent(query: string) {
    console.log('ask.......')
    
    const url = `http://localhost:8877/api/chat?query=${encodeURIComponent(query)}`;

    // Utilisation des EventSource natifs pour gérer le Flux (Streaming) de Spring AI
    const eventSource = new EventSource(url);
    this.aiResponse = '';

    eventSource.onmessage = (event) => {
      // Spring envoie les morceaux de texte au fur et à mesure
      this.aiResponse += event.data;
    };

    eventSource.onerror = (error) => {
      // Le flux est terminé (ou une erreur est survenue)
      eventSource.close();

      // Dès que l'IA a fini d'écrire toute sa réponse, on la fait lire à haute voix !
      this.voiceService.speak(this.aiResponse);
    };
  }

  ngOnDestroy() {
    if (this.sub) this.sub.unsubscribe();
  }
}
