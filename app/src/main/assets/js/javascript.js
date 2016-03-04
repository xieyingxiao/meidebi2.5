 
var DEFAULT_IMAGE_URI = "file:///android_asset/js/default_pic_content_image.png";

var DEFAULT_LOADING_IMAGE_URI = "file:///android_asset/js/default_pic_content_image_loading.gif";

var DEFAULT_LOADING_FAILED_IMAGE_URI = "file:///android_asset/js/default_pic_content_image_failed.png";

function onLoaded()
{
	var allImage = document.querySelectorAll("IMG");

	allImage = Array.prototype.slice.call(allImage, 0);

	allImage.forEach(function(image)
	{
		if (image.src == DEFAULT_LOADING_IMAGE_URI)
		{
			MedebiApp.loadImage(image.getAttribute("original-src"));
		}
	});

}

function setContentPadding(pLeft, pTop, pRight, pBottom)
{
	var content = document.getElementById("content");

	content.style.paddingLeft = pLeft;
	content.style.paddingTop = pTop;
	content.style.paddingRight = pRight;
	content.style.paddingBottom = pBottom;
};

function setFontSize(pFontSize)
{
	document.body.style.fontSize = pFontSize;
};

function onImageClick(pImage,p)
{
		MedebiApp.loadComplte();

	if (pImage.src == DEFAULT_IMAGE_URI || pImage.src == DEFAULT_LOADING_FAILED_IMAGE_URI)
	{
		pImage.src = DEFAULT_LOADING_IMAGE_URI;

		MedebiApp.loadImage(pImage.getAttribute("original-src"));
	}
	else if (pImage.src == DEFAULT_LOADING_IMAGE_URI)
	{
		//nothing
	}
	else
	{
		MedebiApp.openImage(pImage.getAttribute("original-src"),p);
	}
};

function onImageLoadingFailed(pUrl)
{
	var allImage = document.querySelectorAll("IMG");

	allImage = Array.prototype.slice.call(allImage, 0);

	allImage.forEach(function(image)
	{
		if (image.getAttribute("original-src") == decodeURIComponent(pUrl))
		{
			image.src = DEFAULT_LOADING_FAILED_IMAGE_URI;
		}
	});
}

function onImageLoadingComplete(pOldUrl, pNewUrl)
{
	var allImage = document.querySelectorAll("IMG");

	allImage = Array.prototype.slice.call(allImage, 0);

	allImage.forEach(function(image)
	{
			if (image.src == DEFAULT_LOADING_IMAGE_URI)
		{
		if (image.getAttribute("original-src") == decodeURIComponent(pOldUrl))
		{
			image.src = pNewUrl;
		}
		}
		
	});
}