const fs = require('fs');
const path = require('path');

function convertMarkdownToTelegramHTML(markdown) {
    let html = markdown;

    // Convert headers to bold
    html = html.replace(/^### (.*$)/gm, '<b>$1</b>');
    html = html.replace(/^## (.*$)/gm, '<b>$1</b>');
    html = html.replace(/^# (.*$)/gm, '<b>$1</b>');

    // Convert **bold** to <b>bold</b>
    html = html.replace(/\*\*(.*?)\*\*/g, '<b>$1</b>');

    // Convert *italic* to <i>italic</i>
    html = html.replace(/(?<!\*)\*(?!\*)([^*]+?)(?<!\*)\*(?!\*)/g, '<i>$1</i>');

    // Convert `code` to <code>code</code>
    html = html.replace(/`([^`]+?)`/g, '<code>$1</code>');

    // Convert code blocks
    html = html.replace(/```([\s\S]*?)```/g, '<pre>$1</pre>');

    // Convert links [text](url) to <a href="url">text</a>
    html = html.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2">$1</a>');

    // Convert bullet points to simple lists
    html = html.replace(/^[\*\-\+] (.+$)/gm, '• $1');

    // Convert line breaks
    html = html.replace(/\n\n/g, '\n');

    return html.trim();
}

function generateHTMLReleaseNotes(version, notes) {
    const htmlNotes = convertMarkdownToTelegramHTML(notes);

    const htmlContent = `<b>✨ Release [Backend] v${version}</b>

${htmlNotes}`;

    // Save as HTML file
    fs.writeFileSync('release_notes.html', htmlContent, 'utf8');

    // Also save as plain text for Telegram
    fs.writeFileSync('release_notes_telegram.txt', htmlContent, 'utf8');

    console.log('✅ HTML release notes generated successfully!');
    console.log('📁 Files created:');
    console.log('  - release_notes.html');
    console.log('  - release_notes_telegram.txt');
}

// Get command line arguments
const version = process.argv[2];
const notes = process.argv[3];

if (!version || !notes) {
    console.error('❌ Error: Version and notes are required');
    console.error('Usage: node generate-html-release-notes.js <version> <notes>');
    process.exit(1);
}

generateHTMLReleaseNotes(version, notes);
