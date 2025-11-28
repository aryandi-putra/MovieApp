# ✅ Git Commit Checklist

## Before Every Commit - Security Check

### 🔐 Step 1: Check for Secrets

```bash
# Check what files are staged
git status

# Review changes before committing
git diff
```

### 🔍 Step 2: Verify No Secrets in Staged Files

❌ **NEVER commit these:**

```
- API keys in source code
- local.properties
- secrets.properties
- keystore files (*.jks, *.keystore)
- google-services.json (with real keys)
- Database files with user data
- Build outputs (*.apk, *.aab)
```

✅ **Safe to commit:**

```
- Source code (.kt, .java)
- Resource files (layouts, drawables, strings.xml)
- Gradle configuration (build.gradle.kts, settings.gradle.kts)
- Documentation (.md files)
- .gitignore
```

### 📋 Step 3: Pre-Commit Checklist

- [ ] Removed any hardcoded API keys from code
- [ ] API key moved to `local.properties`
- [ ] Verified `local.properties` is in `.gitignore`
- [ ] No keystore files in staging area
- [ ] No `google-services.json` with real keys
- [ ] Build outputs excluded
- [ ] Reviewed `git diff` output
- [ ] Checked `git status` for suspicious files

### 🚀 Step 4: Safe Commit

```bash
# Add specific files (more control)
git add app/src/main/java/com/aryandi/movieapp/

# Or add all (after verification)
git add .

# Commit with descriptive message
git commit -m "feat: implement movie list screen"

# Push to remote
git push origin main
```

---

## 🔥 Emergency: If You Committed Secrets

### If Not Pushed Yet:

```bash
# Remove file from staging
git reset HEAD app/build.gradle.kts

# Or amend last commit
git commit --amend
```

### If Already Pushed:

1. **Revoke the secret immediately!**
    - Invalidate API key
    - Generate new one
    - Update `local.properties`

2. **Remove from Git history** (advanced):

```bash
# Use BFG Repo-Cleaner (recommended)
bfg --delete-files build.gradle.kts

# Or git filter-branch (more complex)
git filter-branch --force --index-filter \
  "git rm --cached --ignore-unmatch path/to/file" \
  --prune-empty --tag-name-filter cat -- --all

# Force push (DANGER: only on your own repos)
git push origin --force --all
```

---

## 🎯 Quick Security Checks

### Check 1: View Staged Files

```bash
git diff --cached --name-only
```

### Check 2: Search for Potential Secrets

```bash
# Search for API key patterns in staged files
git diff --cached | grep -i "api.key"
git diff --cached | grep -i "secret"
git diff --cached | grep -i "password"
```

### Check 3: Verify .gitignore

```bash
# Check if local.properties is ignored
git check-ignore local.properties
# Should output: local.properties

# List all ignored files
git status --ignored
```

---

## 📚 Git Best Practices

### Good Commit Messages

```bash
# Format: <type>: <description>

# Examples:
git commit -m "feat: add movie details screen"
git commit -m "fix: resolve navigation issue"
git commit -m "refactor: extract common UI components"
git commit -m "docs: update README with setup instructions"
git commit -m "test: add unit tests for use cases"
git commit -m "chore: update gradle dependencies"
```

### Commit Types:

- `feat:` - New feature
- `fix:` - Bug fix
- `refactor:` - Code refactoring
- `docs:` - Documentation
- `test:` - Tests
- `chore:` - Maintenance
- `style:` - Code style/formatting
- `perf:` - Performance improvement

---

## 🛡️ Protected by `.gitignore`

Your updated `.gitignore` now protects:

### Secrets & Keys

```
✅ local.properties
✅ secrets.properties
✅ *.jks, *.keystore
✅ google-services.json
✅ apikey.properties
```

### Build Outputs

```
✅ build/
✅ *.apk
✅ *.aab
✅ *.dex
```

### IDE Files

```
✅ .idea/ (most files)
✅ *.iml
✅ .gradle/
```

### System Files

```
✅ .DS_Store
✅ Thumbs.db
✅ *.swp
```

---

## 🔄 Regular Security Maintenance

### Weekly:

- [ ] Review committed files
- [ ] Check for accidentally committed secrets
- [ ] Audit `.gitignore` effectiveness

### Monthly:

- [ ] Rotate API keys
- [ ] Review access logs
- [ ] Update dependencies

### Before Release:

- [ ] Audit entire codebase for secrets
- [ ] Use separate production keys
- [ ] Review ProGuard/R8 configuration
- [ ] Check keystore security

---

## 📞 Quick Reference

### Show What Will Be Committed:

```bash
git diff --cached
```

### Unstage All Files:

```bash
git reset
```

### Unstage Specific File:

```bash
git reset HEAD filename
```

### Check Ignored Files:

```bash
git status --ignored
```

### View Commit History:

```bash
git log --oneline
```

### View File in Last Commit:

```bash
git show HEAD:path/to/file
```

---

## ✅ Final Checklist Before Push

Before running `git push`:

1. [ ] ✅ Reviewed `git status`
2. [ ] ✅ Reviewed `git diff --cached`
3. [ ] ✅ No secrets in staged files
4. [ ] ✅ `local.properties` not staged
5. [ ] ✅ Build files ignored
6. [ ] ✅ Tests pass
7. [ ] ✅ App builds successfully
8. [ ] ✅ Commit message is descriptive
9. [ ] ✅ Ready to push!

```bash
# One final check
git status
git diff --cached --name-only

# If all clear
git push origin main
```

---

## 🎓 Learn More

- [Git Best Practices](https://git-scm.com/book/en/v2)
- [GitHub Security Best Practices](https://docs.github.com/en/code-security)
- [Android App Security](https://developer.android.com/topic/security/best-practices)

---

## 📌 Remember

**The best time to add a file to `.gitignore` was before the first commit.**
**The second best time is now!**

Always:

- ✅ Review before committing
- ✅ Keep secrets in `local.properties`
- ✅ Update `.gitignore` proactively
- ✅ Use meaningful commit messages
- ✅ Test before pushing

**Stay secure! 🔒**
