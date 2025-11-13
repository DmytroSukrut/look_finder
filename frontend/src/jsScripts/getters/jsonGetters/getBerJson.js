import fs from'fs';
import path from 'path';
import { fileURLToPath } from 'url';
import { dirname } from 'path';

const __filename = fileURLToPath(import.meta.url);
const __dirname = dirname(__filename);

(async () => {
    try {
        const url = "https://www.bershka.com/itxrest/3/catalog/store/45109545/40259564/productsArray?categoryId=1010276029&productIds=198409422%2C201665295%2C189276924%2C196958350%2C196951983%2C196951986%2C197299074%2C196946582%2C196946577%2C196946581%2C202798286%2C196958352%2C196958354%2C205025009%2C196951985%2C194635120%2C193750495%2C193750492%2C189276639%2C194635105%2C194635104&appId=1&languageId=-28&locale=sk_SK\n";

        const response = await fetch(url);
        const data = await response.json();

        const folderPath = path.join(__dirname, '../../recJsons/products/ber');
        
        const timeStamp = new Date().toISOString().slice(0, 10);
        const fileName = `bershka_woman_jeans_${timeStamp}`;
        const filePath = path.join(folderPath, `${fileName}.json`);

        fs.writeFileSync(filePath, JSON.stringify(data, null, 2));
    } catch (err) {
        console.error(err.message);
    }

})();