import pygame

#rect objects have (x,y), top, topright, left, center, right bottomleft, bottom, bottomright

WHITE = (255, 255, 255)

class Bird(pygame.sprite.Sprite):
    def __init__(self, height):
        pygame.sprite.Sprite.__init__(self)
        self.image = pygame.Surface((30,30)) #pygame surface you can draw on, 50x50 pixels
        self.image.fill(WHITE)
        self.rect = self.image.get_rect()   #builds rectangle around surface
        self.rect.center = (100, height/2) #starts bird at 0 x coordinate and middle of screen
        self.WINDOW_HEIGHT = height


    def update(self):
        if(self.rect.bottom < self.WINDOW_HEIGHT):
            self.rect.y += 2
        keystate = pygame.key.get_pressed()
        if(keystate[pygame.K_SPACE]):
            if(self.rect.y > 10):
                self.rect.y -= 10

    def think(self):
        inputs = [1, 2, 3, 4]
        
