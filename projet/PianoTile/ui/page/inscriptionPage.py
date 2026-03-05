import pygame
from ui.page.basePage import BasePage
from ui.utils.inputBox import InputBox

class InscriptionPage(BasePage):
    def __init__(self, windowManager):
        super().__init__(windowManager)
        font = self._windowManager.getFontSmall()
        color = self._windowManager.getColor()
        self.color_inactive = color.getGris()
        self.color_active = color.getViolet()
        self.violet = color.getViolet()
        self.blanc = color.getBlanc()
        self.noir = (0, 0, 0)
        self.erreur_inscription = False

        self.input_username = InputBox(0, 0, 400, 50, font, self.color_inactive, self.color_active)
        self.input_password = InputBox(0, 0, 400, 50, font, self.color_inactive, self.color_active, is_password=True)
        self.input_confirmPassword = InputBox(0, 0, 400, 50, font, self.color_inactive, self.color_active, is_password=True)
        self.button_valider = pygame.Rect(0, 0, 400, 60)

    def affichagePage(self):
        window = self._windowManager.getWindow()
        font = self._windowManager.getFontSmall()
        font_title = self._windowManager.getFontTall()
        color = self._windowManager.getColor()

        screen_width = self._windowManager.getScreenWidth()
        screen_height = self._windowManager.getScreenHeight()

        form_width = 400
        form_height = 400
        form_x = (screen_width - form_width) // 2
        form_y = (screen_height - form_height) // 2

        pygame.draw.rect(window, color.getGris(), (form_x - 20, form_y - 70, form_width + 40, form_height + 150))

        titre = font_title.render("Inscription", True, self.noir)
        window.blit(titre, titre.get_rect(center=(screen_width // 2, form_y - 20)))

        spacing = 100
        input_x = form_x + (form_width - 280) // 2
        input_y = form_y

        labels = ["Nom d'utilisateur :", "Mot de passe :", "Confirmer le mot de passe :"]
        inputs = [self.input_username, self.input_password, self.input_confirmPassword]

        for i in range(3):
            label = font.render(labels[i], True, self.noir)
            window.blit(label, (input_x - 60, input_y + 30 + i * spacing))
            inputs[i].rect.topleft = (input_x - 60, input_y + 70 + i * spacing)
            inputs[i].draw(window)

        self.button_valider.topleft = (input_x - 60, input_y + 50 + 3 * spacing)
        pygame.draw.rect(window, self.violet, self.button_valider)
        txt = font.render("Valider", True, self.blanc)
        window.blit(txt, txt.get_rect(center=self.button_valider.center))

        if self.erreur_inscription:
            erreur_texte = font.render("Impossible de valider l'inscription", True, (255, 0, 0))
            window.blit(erreur_texte, (input_x - 60, input_y + 120 + 3 * spacing))

    def handle_event(self, event):
        self.input_username.handle_event(event)
        self.input_password.handle_event(event)
        self.input_confirmPassword.handle_event(event)
        self._windowManager.getInterface().setUpdate(True)
