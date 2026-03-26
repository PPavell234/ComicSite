// Для Скрипт для быстрого открытие страниц

import fs from "fs";
import path from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// Путь к router/index.js
const routerPath = path.join(__dirname, "../src/router/index.js");
// Путь к файлу для скриптов открытия
const openPagesPath = path.join(__dirname, "../open-pages.json");

// Проверяем, существует ли файл router/index.js
if (!fs.existsSync(routerPath)) {
  console.error("Файл router/index.js не найден!");
  console.error(`Искали: ${routerPath}`);
  process.exit(1);
}

// Читаем router/index.js
const routerContent = fs.readFileSync(routerPath, "utf8");

// Извлекаем маршруты из файла
const routePattern = /path:\s*"([^"]+)",\s*name:\s*"([^"]+)"/g;
const routes = [];

let match;
while ((match = routePattern.exec(routerContent)) !== null) {
  const pathUrl = match[1];
  const name = match[2];
  if (pathUrl !== "/comic/:id/read") {
    routes.push({ path: pathUrl, name });
  }
}

console.log(
  "Найдены маршруты:",
  routes.map((r) => `${r.name} (${r.path})`).join(", "),
);

// Создаем объект с командами для открытия
const baseUrl = "http://localhost:5173";
const openCommands = {};

routes.forEach((route) => {
  const url = route.path === "/" ? baseUrl : `${baseUrl}${route.path}`;
  const commandName = route.name.toLowerCase();
  openCommands[commandName] = {
    url: url,
    command: `start ${url}`,
    path: route.path,
  };
});

// Добавляем специальный для ComicReader
openCommands["comicreader"] = {
  url: "http://localhost:5173/comic/69b7ed632bc986693aeb196f/read",
  command: "start http://localhost:5173/comic/69b7ed632bc986693aeb196f/read",
  path: "/comic/:id/read",
};

// Сохраняем в файл
fs.writeFileSync(openPagesPath, JSON.stringify(openCommands, null, 2));

console.log("\nФайл open-pages.json создан!");
console.log(`Путь: ${openPagesPath}`);
console.log("\nДоступные страницы:");
Object.keys(openCommands).forEach((key) => {
  console.log(`   ${key}: ${openCommands[key].url}`);
});
