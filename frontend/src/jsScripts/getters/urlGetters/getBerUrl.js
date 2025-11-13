import puppeteer from 'puppeteer-extra';
import StealthPlugin from 'puppeteer-extra-plugin-stealth';

puppeteer.use(StealthPlugin());

(async () => {
    const browser = await puppeteer.launch({
        headless: true,
        args: [
            '--no-sandbox',
            '--disable-setuid-sandbox',
            '--disable-blink-features=AutomationControlled'
        ],
    });
    const page = await browser.newPage();

    console.log("Page loading");
    await page.goto('https://www.bershka.com/sk/zeny/oblecenie/dzinsy-n3821.html', {
        waitUntil: 'networkidle2'
    });

    console.log("Page loaded");

    await page.evaluate(() => location.reload());

    let needed_url = "https://www.bershka.com/";

    page.on('request', req => {
        if(req.url().includes('https://www.bershka.com/itxrest/3/')) {
            if(req.url().includes('productsArray?')) {
                console.log('!!!!!!!!!!!!!!!->', req.url());
                needed_url = req.url();
                browser.close();
                return;
            }
        }
        console.log('➡️', req.url());
    });

})();