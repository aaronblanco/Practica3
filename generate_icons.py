from PIL import Image, ImageDraw
import os

# Definir resoluciones
resolutions = {
    'mipmap-mdpi': 48,
    'mipmap-hdpi': 72,
    'mipmap-xhdpi': 96,
    'mipmap-xxhdpi': 144,
    'mipmap-xxxhdpi': 192
}

def draw_shopping_cart(size):
    """Dibujar un carrito de compras"""
    # Crear imagen con fondo naranja
    img = Image.new('RGB', (size, size), color='#FF6B35')
    draw = ImageDraw.Draw(img)

    # Calcular proporciones basadas en tamaño
    padding = size // 8
    cart_width = size - (padding * 2)
    cart_start_x = padding

    # Mango
    handle_y = padding + 2
    handle_height = size // 6
    draw.rectangle(
        [(cart_start_x + 4, handle_y),
         (cart_start_x + cart_width - 4, handle_y + handle_height)],
        fill='white'
    )

    # Carrito principal
    cart_y = handle_y + handle_height
    cart_height = size // 3
    draw.rectangle(
        [(cart_start_x + 4, cart_y),
         (cart_start_x + cart_width - 4, cart_y + cart_height)],
        fill='white'
    )

    # Base del carrito
    base_height = size // 20
    draw.rectangle(
        [(cart_start_x + 4, cart_y + cart_height),
         (cart_start_x + cart_width - 4, cart_y + cart_height + base_height)],
        fill='white'
    )

    # Ruedas
    wheel_y = cart_y + cart_height + base_height + 4
    wheel_radius = size // 12

    # Rueda izquierda
    left_wheel_x = cart_start_x + 10
    draw.ellipse(
        [(left_wheel_x - wheel_radius, wheel_y - wheel_radius),
         (left_wheel_x + wheel_radius, wheel_y + wheel_radius)],
        fill='white'
    )

    # Rueda derecha
    right_wheel_x = cart_start_x + cart_width - 10
    draw.ellipse(
        [(right_wheel_x - wheel_radius, wheel_y - wheel_radius),
         (right_wheel_x + wheel_radius, wheel_y + wheel_radius)],
        fill='white'
    )

    return img

# Crear directorio base si no existe
base_dir = r'C:\Users\aaron\StudioProjects\Practica3\app\src\main\res'

# Generar iconos para cada resolución
for dir_name, size in resolutions.items():
    dir_path = os.path.join(base_dir, dir_name)

    # Crear imagen cuadrada
    img = draw_shopping_cart(size)

    # Guardar como ic_launcher
    icon_path = os.path.join(dir_path, 'ic_launcher.png')
    img.save(icon_path)
    print(f"Creado: {icon_path}")

    # Crear versión redonda
    img_round = Image.new('RGB', (size, size), color='#FF6B35')

    # Crear máscara circular
    mask = Image.new('L', (size, size), 0)
    mask_draw = ImageDraw.Draw(mask)
    mask_draw.ellipse([(0, 0), (size, size)], fill=255)

    # Aplicar máscara
    output = Image.new('RGB', (size, size), '#FF6B35')
    output.paste(img, (0, 0))
    output.putalpha(mask)

    icon_round_path = os.path.join(dir_path, 'ic_launcher_round.png')
    output.save(icon_round_path)
    print(f"Creado: {icon_round_path}")

print("¡Iconos generados correctamente!")

