import fs from "fs";
import { exec } from "child_process";
import { fileURLToPath } from "url";
import path from "path";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const openPagesPath = path.join(__dirname, "../open-pages.json");

if (!fs.existsSync(openPagesPath)) {
  console.error(
    "Файл open-pages.json не найден! Сначала выполните: npm run sync",
  );
  process.exit(1);
}

const openPages = JSON.parse(fs.readFileSync(openPagesPath, "utf8"));
const pageName = process.argv[2];

if (!pageName) {
  console.log("Доступные страницы:");
  Object.keys(openPages).forEach((key) => {
    console.log(`   ${key}`);
  });
  console.log("\nПример: npm run open comicp");
  process.exit(0);
}

const foundKey = Object.keys(openPages).find(
  (key) => key.toLowerCase() === pageName.toLowerCase(),
);

if (foundKey && openPages[foundKey]) {
  const page = openPages[foundKey];
  console.log(`Открываю: ${page.url}`);

  // Для Windows
  const command = `start ${page.url}`;
  exec(command);
} else {
  console.log(`Страница "${pageName}" не найдена`);
  console.log("Доступные страницы:");
  Object.keys(openPages).forEach((key) => {
    console.log(`   ${key}`);
  });
}

//Документация как работать
//Чтобы открыть пиши npm run open mainPage
//Чтобы обновить пиши npm run syn
